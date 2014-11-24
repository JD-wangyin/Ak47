package com.wangyin.ak47.example;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.service.ForwardService;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.stub.SimpleStub;
import com.wangyin.ak47.pipes.http.SimpleHttpPipe;
import com.wangyin.ak47.pipes.http.SimpleHttpRequest;
import com.wangyin.ak47.pipes.http.SimpleHttpResponse;


public class SimpleForwardExample {
    private static final Logger log = new Logger(SimpleForwardExample.class);
    
    public static void main(String args[]) throws Exception{
        SimpleHttpPipe ep = new SimpleHttpPipe();
        
        SimpleStub<SimpleHttpRequest, SimpleHttpResponse> stub = ep.createSimpleStub(8555);
        
        stub.addService("forward", new ForwardService<SimpleHttpRequest, SimpleHttpResponse>(ep, 
                "www.jd.com", 80));
        
        stub.addService("user", new Service<SimpleHttpRequest, SimpleHttpResponse>(){


            @Override
            public void doService(Request<SimpleHttpRequest> request,
                    Response<SimpleHttpResponse> response) throws Exception {
                log.info("q: {}", request.pojo());
                log.info("r: {}", response.pojo());
            }
        });
        
        stub.start();
        stub.hold();
        
    }

}
