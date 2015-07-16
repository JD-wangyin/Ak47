package com.wangyin.ak47.pipes.http;

import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;

/**
 * 简单实现
 * 
 * @author wyhubingyin
 * 
 */
public final class SimpleHttpPipe extends AbstractHttpPipe<SimpleHttpRequest, 
        SimpleHttpResponse>{
//    private static final Logger log = new Logger(SimpleHttpPipe.class);

    @Override
    public void decodeHttpRequest(SimpleHttpRequest httpReq, 
            Request<SimpleHttpRequest> request) 
            throws Exception{
        request.pojo(httpReq);
    }

    @Override
    public void encodeHttpRequest(Request<SimpleHttpRequest> request, 
            SimpleHttpRequest httpReq) 
            throws Exception {
        httpReq.copyOf(request.pojo());
    }

    @Override
    public void decodeHttpResponse(SimpleHttpResponse httpRes, 
            Response<SimpleHttpResponse> response) {
        response.pojo(httpRes);
    }

    @Override
    public void encodeHttpResponse(Response<SimpleHttpResponse> response, 
            SimpleHttpResponse httpRes) {
        httpRes.copyOf(response.pojo());
    }
    
    
}
