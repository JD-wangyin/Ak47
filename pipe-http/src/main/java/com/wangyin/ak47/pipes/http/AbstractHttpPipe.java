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
 * 
 * @param <Q>       Request POJO
 * @param <R>       Response POJO
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
     * HttpRequest →→→ POJO
     * 
     * @param httpreq       HTTP Request object that offer simple API
     * @param request       Request with POJO
     * @throws Exception    in any case
     */
    public abstract void decodeHttpRequest(SimpleHttpRequest httpreq, 
            Request<Q> request) throws Exception;

    
    @Override
    public void encodeRequest(Request<Q> request, Buffer buf) throws Exception { 
        
        SimpleHttpRequest httpreq = new SimpleHttpRequest();
        encodeHttpRequest(request, httpreq);
        
        if( httpreq.getContent() != null && 
                httpreq.getHeaderFirst("content-length") == null ){
            int contentLength = httpreq.getContent().length;
            httpreq.setOrAddFirstHeader("content-length", 
                    String.valueOf(contentLength));
        }
        
        buf.writeBytes(httpreq.buildFullBytes());
    }
    

    /**
     * POJO →→→ HttpRequest
     * 
     * @param request       Request with POJO
     * @param httpreq       HTTP Request object that offer simple API
     * @throws Exception    in any case
     */
    public abstract void encodeHttpRequest(Request<Q> request, 
            SimpleHttpRequest httpreq) throws Exception;

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
    
    
    /**
     * HttpResponse →→→ POJO
     * 
     * @param httpres       HTTP Response object that offer simple API
     * @param response      Response with POJO
     * @throws Exception    In any case
     */
    public abstract void decodeHttpResponse(SimpleHttpResponse httpres, 
            Response<R> response) throws Exception;

    @Override
    public void encodeResponse(Response<R> response, Buffer buf) throws Exception {
        SimpleHttpResponse httpRes = new SimpleHttpResponse();
        encodeHttpResponse(response, httpRes);
        
        if( httpRes.getContent() != null && 
                httpRes.getHeaderFirst("content-length") == null ){
            int contentLength = httpRes.getContent().length;
            httpRes.setOrAddFirstHeader("content-length", 
                    String.valueOf(contentLength));
        }
        
        if( httpRes.getHeaderFirst("connection") == null ){
            httpRes.setOrAddFirstHeader("connection", "keep-alive");
        }
        

        buf.writeBytes(httpRes.buildFullBytes());
    }

    /**
     * POJO →→→ HttpRequest
     * 
     * @param response      Response with POJO
     * @param httpres       HTTP Response object that offer simple API
     * @throws Exception    In any case
     */
    public abstract void encodeHttpResponse(Response<R> response, 
            SimpleHttpResponse httpres) throws Exception;
    
    
}
