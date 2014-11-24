package com.wangyin.ak47.core.util;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleSessionAttr extends Attributes implements SessionAttr {
    
    private AtomicLong requestOrder = new AtomicLong(1);
    
    public SimpleRequestAttr newRequestAttr(){
        return new SimpleRequestAttr(requestOrder.getAndIncrement());
    }
 
}
