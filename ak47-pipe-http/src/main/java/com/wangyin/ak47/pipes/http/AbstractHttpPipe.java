package com.wangyin.ak47.pipes.http;


import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Buffer;

/**
 * 抽象HttpPipe基类，
 * 基于ApcheComponent-HttpCore —— httpcore内核设计太糟糕了，放弃。
 * 
 * 现用自制SimpleHttpParser，不支持pipeline方式。
 * 
 * @author wyhubingyin
 * @date 2014年2月10日
 * 
 * @param <Q>
 * @param <R>
 */
public abstract class AbstractHttpPipe<Q, R> extends Pipe<Q, R>{
    private static final Logger log = new Logger(AbstractHttpPipe.class);
    

    @Override
    public void decodeRequest(Buffer buf, Request<Q> request) throws Exception {
        SimpleHttpRequest httpreq = SimpleHttpParser.parseRequest(buf);
        if( null == httpreq ){
            log.debug("parseHttpRequest fail.");
        }else{
            decodeHttpRequest(httpreq, request);
        }
        
    }
    
    /**
     * 这里可将HttpRequest转化为任何对象。
     * 
     * @param request   HttpRequest有完整的操作http协议的API
     * @param pojos     任何对象
     * @throws Exception 
     */
    public abstract void decodeHttpRequest(SimpleHttpRequest httpreq, Request<Q> request) throws Exception;

    
    @Override
    public void encodeRequest(Request<Q> request, Buffer buf) throws Exception { 
        
        SimpleHttpRequest httpreq = new SimpleHttpRequest();
        encodeHttpRequest(request, httpreq);
        
        if( httpreq.getContent() != null && httpreq.getHeaderFirst("Content-Length") == null ){
            int contentLength = httpreq.getContent().length;
            httpreq.setOrAddFirstHeader("Content-Length", String.valueOf(contentLength));
        }
        
        buf.writeBytes(httpreq.buildFullBytes());
    }
    

    /**
     * 这里需要将将任何对象转化为response
     * 
     * @param pojos     任何对象
     * @param response  SimpleHttpResponse
     */
    public abstract void encodeHttpRequest(Request<Q> request, SimpleHttpRequest httpreq) throws Exception;

    @Override
    public void decodeResponse(Buffer buf, Response<R> response) throws Exception {
        
        
        SimpleHttpResponse httpRes = SimpleHttpParser.parseResponse(buf);
        if( null == httpRes ){
            log.debug("parseHttpResponse fail.");
        }else{
            decodeHttpResponse(httpRes, response);
        }
        
        
//        int readableBytes = buf.readableBytes();
//        if( readableBytes > 0 ){
//            int readerIndex = buf.readerIndex();
//            byte[] bytes = new byte[readableBytes];
//            buf.readBytes(bytes);
//            SimpleHttpResponse httpRes = SimpleHttpParser.parseResponse(bytes);
//            
////            ////FIXME
////            int readerIndex = buf.readerIndex();
////            buf.readerIndex(readerIndex+readableBytes);
////            SimpleHttpResponse httpRes = new SimpleHttpResponse();
////            httpRes.setContent("It works!".getBytes());
////            ////FIXME
//            
//            
//            if( null == httpRes ){
//                log.debug("parseHttpResponse fail, rollback.");
//                buf.readerIndex(readerIndex);
//            }else{
//                decodeHttpResponse(httpRes, response);
//            }
//        }else{
//            log.warn("NOTHING to read.");
//        } 
    }
    
    public abstract void decodeHttpResponse(SimpleHttpResponse httpres, Response<R> response) throws Exception;

    @Override
    public void encodeResponse(Response<R> response, Buffer buf) throws Exception {
        SimpleHttpResponse httpRes = new SimpleHttpResponse();
        encodeHttpResponse(response, httpRes);
        
        if( httpRes.getContent() != null && httpRes.getHeaderFirst("Content-Length") == null ){
            int contentLength = httpRes.getContent().length;
            httpRes.setOrAddFirstHeader("Content-Length", String.valueOf(contentLength));
        }

        buf.writeBytes(httpRes.buildFullBytes());
    }

    public abstract void encodeHttpResponse(Response<R> response, SimpleHttpResponse httpres) throws Exception;
}
