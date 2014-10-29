package com.wangyin.ak47.example;


import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.stub.SimpleStub;
import com.wangyin.ak47.pipes.http.SimpleHttpPipe;
import com.wangyin.ak47.pipes.http.SimpleHttpRequest;
import com.wangyin.ak47.pipes.http.SimpleHttpResponse;


public class SimpleHttpStubExample {
    private static final Logger log = new Logger(SimpleHttpStubExample.class);

    public static void main(String[] args) throws Exception {
        
        // new a Pipe
        SimpleHttpPipe pipe = new SimpleHttpPipe();
        
        SimpleStub<SimpleHttpRequest, SimpleHttpResponse> stub = pipe.createSimpleStub(8055);
        
        stub.addService("myservice", new Service<SimpleHttpRequest, SimpleHttpResponse>(){

            @Override
            public void doService(Request<SimpleHttpRequest> request,
                    Response<SimpleHttpResponse> response) throws Exception {
                SimpleHttpRequest httpreq = request.pojo();
                log.info("Got a request, url is {}.", httpreq.getUrl());
                
                SimpleHttpResponse httpres = new SimpleHttpResponse();
                String content = "Your request url is "+httpreq.getUrl();
                httpres.setContent(content.getBytes());
                
                response.pojo(httpres);
            }

            
        });
        
        stub.start();
        stub.hold();
    }

}













