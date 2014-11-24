package com.wangyin.ak47.core.message;


import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.util.RequestAttr;
import com.wangyin.ak47.core.util.SessionAttr;


public class SimpleRequest<Q> implements Request<Q> { 
    
    private long order;
    private Message<Q> msg;
    
    public SimpleRequest(Message<Q> msg){
        this.msg = msg;
        order = msg.getRequestAttr().order();
    }

    @Override
    public long order() {
        return order;
    }

    @Override
    public boolean hasPojo() {
         return msg.hasPojo();
    }

    @Override
    public Q pojo() {
        return msg.getPojo();
    }

    @Override
    public void pojo(Q pojo) {
        msg.setPojo(pojo);
    }

    @Override
    public RequestAttr requestAttr() {
        return msg.getRequestAttr();
    }
    
    @Override
    public SessionAttr sessionAttr() {
        return msg.getSessionAttr();
    }
    
}
