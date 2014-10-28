package com.wangyin.ak47.example;


import com.wangyin.ak47.core.driver.SimpleDriver;
import com.wangyin.ak47.pipes.http.SimpleHttpPipe;
import com.wangyin.ak47.pipes.http.SimpleHttpRequest;
import com.wangyin.ak47.pipes.http.SimpleHttpResponse;

public class SimpleHttpDriverExample {

    public static void main(String[] args) throws Exception {
        
        // new a Pipe
        SimpleHttpPipe pipe = new SimpleHttpPipe();
        
        // create a simple driver
        SimpleDriver<SimpleHttpRequest, SimpleHttpResponse> driver = 
                pipe.createSimpleDriver("www.jd.com", 80);
//                pipe.createSimpleDriver("localhost", 8055);
        
        // prepare a request
        SimpleHttpRequest httpreq = new SimpleHttpRequest("GET", "/");
        
        // send it, and get a response
        SimpleHttpResponse httpres = driver.send(httpreq);
        
        // finally check the res.
        System.out.println(httpres.getStartLine());
        System.out.println(httpres.getContent().length);
        System.out.println(httpres.getStatusCode());
        
    }

}













