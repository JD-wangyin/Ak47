package com.wangyin.ak47.example;


import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.stub.SimpleStub;
import com.wangyin.ak47.pipes.http.SimpleHttpPipe;
import com.wangyin.ak47.pipes.http.SimpleHttpRequest;
import com.wangyin.ak47.pipes.http.SimpleHttpResponse;


public class SimpleHttpStubExample {

    public static void main(String[] args) throws Exception {
        
        // new a Pipe
        SimpleHttpPipe pipe = new SimpleHttpPipe();
        
        // create a stub
        SimpleStub<SimpleHttpRequest, SimpleHttpResponse> stub = pipe.createSimpleStub(8055);
        
        // add a service
        stub.addService("myservice", new Service<SimpleHttpRequest, SimpleHttpResponse>(){
            @Override
            public void doService(Request<SimpleHttpRequest> request,
                    Response<SimpleHttpResponse> response) throws Exception {
                SimpleHttpRequest httpreq = request.pojo();
                
                SimpleHttpResponse httpres = new SimpleHttpResponse();
                String content = "Hello Ak47! Your request url is "+httpreq.getUrl();
                httpres.setContent(content.getBytes());
                response.pojo(httpres);
            }
        });
        
        // finally start and hold it.
        stub.start();
        stub.hold();
    }

}













