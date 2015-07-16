package com.wangyin.ak47.pipes.http;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.wangyin.ak47.common.ByteUtil;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Buffer;

/**
 * 简单的Http协议解析器
 * 无依赖第三方解析库
 * 支持 chunked
 * 
 * @author wyhubingyin
 *
 */
public class SimpleHttpParser {
    private static final Logger log = new Logger(SimpleHttpParser.class);
    
    // \n
    public static final byte LF = 10;
    // \r
    public static final byte CR = 13;
    // \r\n
    public static final byte[] CRLF_BYTE_ARRAY = new byte[]{CR,LF};
    // \r\n string
    public static final String CRLF_STRING = "\r\n";

    // 一行最大长度
    private static final int MAX_HEADER_LINE_LENGTH_LIMIT = 4 * 1024;
    
    // startline + header 的最大字节数
    private static final int MAX_HEADERS_BLOCK_SIZE_LIMIT = 8 * 1024; 
    
    

    /**
     * Buffer →→→ HttpRequest
     * 
     * @param buf       Buffer
     * @return          HttpRequest
     */
    public static SimpleHttpRequest parseRequest(Buffer buf){
        SimpleHttpMessage shm = parseMessage(buf);
        if( null == shm ){
            return null;
        }
        return SimpleHttpRequest.valueOf(shm);
    }
    
    
    /**
     * Buffer →→→ HttpResponse
     * 
     * @param buf       Buffer
     * @return          HttpResponse
     */
    public static SimpleHttpResponse parseResponse(Buffer buf){
        SimpleHttpMessage shm = parseMessage(buf);
        if( null == shm ){
            return null;
        }
        return SimpleHttpResponse.valueOf(shm);
    }
    
    
    /**
     * Buffer →→→ HttpMessage
     * 
     * @param buf       Buffer
     * @return          HttpMessage
     */
    public static SimpleHttpMessage parseMessage(Buffer buf){
        
        int beginIndex = buf.readerIndex();
        int allReadableNum = buf.readableBytes();
        int firstTryReadableNum = Math.min(allReadableNum, MAX_HEADERS_BLOCK_SIZE_LIMIT);
        byte[] firstTryBytes = new byte[firstTryReadableNum];
        buf.readBytes(firstTryBytes);
        
        SimpleHttpMessage shm = new SimpleHttpMessage();
        
        
        // readOffset
        int readOffset = 0;
        
        // parse startLine
        for( ; readOffset < firstTryBytes.length-1 && readOffset < MAX_HEADER_LINE_LENGTH_LIMIT; readOffset++ ){ 
            if( firstTryBytes[readOffset] == CR && firstTryBytes[readOffset+1] == LF){
                break;
            }
        }
        if( readOffset == MAX_HEADER_LINE_LENGTH_LIMIT ){
            log.warn("decode HttpMessage fail. StartLine is too long.");
            buf.readerIndex(beginIndex);
            return null;
        }
        if( readOffset >= firstTryBytes.length-1 ){
            log.debug("Need more read in parsing start line.");
            buf.readerIndex(beginIndex);
            return null;
        }
        
        String startLine = new String(firstTryBytes, 0, readOffset);

//        //下面的代码由于性能问题舍弃掉
//        String[] startLineItems = startLine.split(" ",3);
//        if( startLineItems.length != 3 ){
//            log.warn("Malformed first line.");
//            buf.readerIndex(beginIndex);
//            return null;
//        }else if( !startLineItems[2].startsWith("HTTP") && !startLineItems[0].startsWith("HTTP")){
//            log.warn("Malformed first line, may NOT http.");
//            buf.readerIndex(beginIndex);
//            return null;
//        }
        StringTokenizer strToken = new StringTokenizer(startLine, " ");
        if( strToken.countTokens() < 3 ){
          log.warn("Malformed first line: {}.", startLine);
          buf.readerIndex(beginIndex);
          return null;
        }
        shm.setStartLine(startLine);
        
        // parse headers
        readOffset = readOffset + 2;
        while( readOffset < firstTryBytes.length-1 ){
            if( firstTryBytes[readOffset] == CR && firstTryBytes[readOffset+1] == LF ){
                // header end
                break;
            }

            int lineBeginIndex = readOffset;
            int lineReadLen = 0;
            while( readOffset < firstTryBytes.length-1 && lineReadLen < MAX_HEADER_LINE_LENGTH_LIMIT ){
                if( firstTryBytes[readOffset] == CR && firstTryBytes[readOffset+1] == LF ){
                    break;
                }
                readOffset++;
                lineReadLen++;
            }
            if( readOffset >= firstTryBytes.length-1 ){
                log.debug("Need more read in parsing headers.");
                buf.readerIndex(beginIndex);
                return null;
            }
            if( lineReadLen == MAX_HEADER_LINE_LENGTH_LIMIT ){
                log.error("decode HttpMessage fail. Header is too long.");
                buf.readerIndex(beginIndex);
                return null;
            }
            readOffset = readOffset + 2;
            String headerLine = new String(firstTryBytes, lineBeginIndex, lineReadLen);
            
//            // 由于split的性能问题，舍弃
//            String[] headerLineItems = headerLine.split(":", 2);
//            if( headerLineItems.length != 2 ){
//                log.warn("decode HttpMessage fail. Malformed header line.");
//                buf.readerIndex(beginIndex);
//                return null;
//            }
//            String skey = headerLineItems[0].trim().toLowerCase();
//            String value = headerLineItems[1].trim();
            StringTokenizer headerToken = new StringTokenizer(headerLine, ":");
            if( headerToken.hasMoreTokens() ){
                String key = headerToken.nextToken();
                if( headerToken.hasMoreTokens() ){
                    String value = headerToken.nextToken().trim();
//                    key = StringUtil2.asciiToLowerCase(key);
                    shm.addHeader(key, value);
                }else{
                    log.warn("HttpMessage decode fail. Malformed header line[{}].", headerLine);
                    buf.readerIndex(beginIndex);
                    return null;
                }
            }else{
                 log.warn("HttpMessage decode fail. Malformed header line[{}].", headerLine);
                 buf.readerIndex(beginIndex);
                 return null;
            }
            
        }
        
        if( readOffset >= firstTryBytes.length-1 ){
            log.debug("Need more read in parsing headers.");
            buf.readerIndex(beginIndex);
            return null;
        }
        
        
        
        // parse content
        readOffset = readOffset + 2;
        String sContentLength = shm.getHeaderFirst("Content-Length");
        String transferEncoding = shm.getHeaderFirst("Transfer-Encoding");
        if( null != sContentLength ){
            // check content-length
            int contentLength = Integer.parseInt(sContentLength);
            if( contentLength > 0 ){
                if( readOffset + contentLength > allReadableNum ){
                    log.debug("Need more read in parsing content.");
                    buf.readerIndex(beginIndex);
                    return null;
                }else{
                    int diff = readOffset + contentLength - firstTryReadableNum;
                    if( diff >= 0 ){
                        byte[] cntBytes = new byte[contentLength];
                        ByteUtil.copy(firstTryBytes, readOffset, cntBytes, 0, firstTryReadableNum - readOffset);
                        buf.readBytes(cntBytes, firstTryReadableNum - readOffset, diff);
                        
                        shm.setContent(cntBytes);
                    }else if( diff < 0 ){
                        byte[] cnt = new byte[contentLength];
                        ByteUtil.copy(firstTryBytes, readOffset, cnt, 0, contentLength);
                        
                        shm.setContent(cnt);
                        buf.readerIndex(beginIndex + readOffset + contentLength);
                    }
                    
                }
            }
        }else if( transferEncoding != null && "chunked".equals(transferEncoding) ){
            // read chunked 
            // 如果一个HTTP消息（请求消息或应答消息）的Transfer-Encoding消息头的值为chunked，那么，消息体由数量未定的块组成，并以最后一个大小为0的块为结束。
            // 每一个非空的块都以该块包含数据的字节数（字节数以十六进制表示）开始，跟随一个CRLF （回车及换行），然后是数据本身，最后块CRLF结束。在一些实现中，块大小和CRLF之间填充有白空格（0x20）。
            // 最后一块是单行，由块大小（0），一些可选的填充白空格，以及CRLF。最后一块不再包含任何数据，但是可以发送可选的尾部，包括消息头字段。
            // 消息最后以CRLF结尾。
            // 示例：
            //            HTTP/1.1 200 OK
            //            Content-Type: text/plain
            //            Transfer-Encoding: chunked
            //
            //            25
            //            This is the data in the first chunk
            //
            //            1C
            //            and this is the second one
            //
            //            3
            //            con
            //            8
            //            sequence
            //            0

            int diff = allReadableNum - firstTryReadableNum;
            byte[] cntBytes = null;
            if( diff > 0 ){
                cntBytes = new byte[allReadableNum - readOffset];
                ByteUtil.copy(firstTryBytes, readOffset, cntBytes, 0, firstTryReadableNum-readOffset);
                
                buf.readBytes(cntBytes, firstTryReadableNum-readOffset, diff);
            }else{ // diff == 0
                cntBytes = new byte[firstTryReadableNum - readOffset];
                ByteUtil.copy(firstTryBytes, readOffset, cntBytes, 0, firstTryReadableNum - readOffset);
            }
            
            
            int cntReadOffset = 0;
            
            List<byte[]> chunkedList = new ArrayList<byte[]>();
            while( cntReadOffset < cntBytes.length-1 ){ 
                
                // 读出块大小
                int lineBeginIndex = cntReadOffset;
                int lineReadLen = 0;
                while( cntReadOffset < cntBytes.length-1 && lineReadLen < MAX_HEADER_LINE_LENGTH_LIMIT ){
                    if( cntBytes[cntReadOffset] == CR && cntBytes[cntReadOffset+1] == LF ){
                        break;
                    }
                    cntReadOffset++;
                    lineReadLen++;
                }
                if( cntReadOffset >= cntBytes.length-1 ){
                    log.debug("Need more read in parsing chunked body.");
                    buf.readerIndex(beginIndex);
                    return null;
                }
                if( lineReadLen == MAX_HEADER_LINE_LENGTH_LIMIT ){
                    log.error("decode HttpMessage fail. chunked size is too long.");
                    buf.readerIndex(beginIndex);
                    return null;
                }
                cntReadOffset = cntReadOffset + 2;
                String chunkedSizeLine = new String(cntBytes, lineBeginIndex, lineReadLen);
                
                log.error("chunkedSizeLine: {}", chunkedSizeLine);
                int chunkedSize = Integer.parseInt(chunkedSizeLine, 16);
                if( chunkedSize == 0 ){
                    // last chunked
                    break;
                }
                
                if( cntReadOffset + chunkedSize + 2 > cntBytes.length ){
                    log.debug("Need more read in parsing chunked body.");
                    buf.readerIndex(beginIndex);
                    return null;
                }
                
                byte[] chunked = ByteUtil.copyOf(cntBytes, cntReadOffset, chunkedSize);
                chunkedList.add(chunked);
                
                // drop crlf
                cntReadOffset = cntReadOffset + chunkedSize + 2;
            }
            
            if( cntReadOffset >= cntBytes.length-1 ){
                log.debug("Need more read in parsing chunked body.");
                buf.readerIndex(beginIndex);
                return null;
            }
            
            // 拼装body
            
            byte[] content = ByteUtil.merge( chunkedList );
            shm.setContent(content);
            
        }else{
//            // 如果没有content-length，并且非chunked，这里肯定是一个错误的HttpMessage。
//            // 这里采取高容错率做法，能读多少就读多少。可能不太好。
//            log.error("Http response header does NOT has 'content-length' or 'chunked'. Just read as mush as possible of content.");
//            
//            int diff = allReadableNum - firstTryReadableNum;
//            byte[] cntBytes = null;
//            if( diff > 0 ){
//                cntBytes = new byte[allReadableNum - readOffset];
//                ByteUtil.copy(firstTryBytes, readOffset, cntBytes, 0, firstTryReadableNum-readOffset);
//                
//                buf.readBytes(cntBytes, firstTryReadableNum-readOffset, diff);
//            }else{ // diff == 0
//                cntBytes = new byte[firstTryReadableNum - readOffset];
//                ByteUtil.copy(firstTryBytes, readOffset, cntBytes, 0, firstTryReadableNum - readOffset);
//            }
//            
//            shm.setContent(cntBytes);
            
            // 如果没有content-length，并且非chunked，那么就认为只有header。
            // 也就是说，啥也不做。
        }
        
        // return httpmessage
        return shm;
    }
    
    
    
}
