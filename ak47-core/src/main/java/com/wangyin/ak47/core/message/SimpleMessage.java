package com.wangyin.ak47.core.message;


import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.netty.NettyBuffer;
import com.wangyin.ak47.core.util.RequestAttr;
import com.wangyin.ak47.core.util.SessionAttr;


public class SimpleMessage<T> implements Message<T> {
    
    protected T pojo;
    protected Buffer buffer;
    protected RequestAttr requestAttr;
    protected SessionAttr sessionAttr;

    public SimpleMessage(){
        this(null, new NettyBuffer());
    }

    public SimpleMessage(T pojo){
        this(pojo, new NettyBuffer());
    }
    
    public SimpleMessage(Buffer buffer){
        this(null, buffer);
    }
    
    public SimpleMessage(T pojo, Buffer buffer){
        this.pojo = pojo;
        this.buffer = buffer;
    }

    @Override
    public boolean hasPojo() {
        return pojo != null;
    }

    @Override
    public T getPojo() {
        return pojo;
    }

    @Override
    public void setPojo(T t) {
        this.pojo = t;
    }

    @Override
    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }
    
    @Override
    public void clear(){
        this.buffer.clear();
        this.pojo = null;
    }
    
    @Override
    public boolean hasBuffer() {
        return buffer != null;
    }

    @Override
    public RequestAttr getRequestAttr() {
        return requestAttr;
    }
    
    public void setRequestAttr(RequestAttr requestAttr) {
        this.requestAttr = requestAttr;
    }

    @Override
    public SessionAttr getSessionAttr() {
        return sessionAttr;
    }

    public void setSessionAttr(SessionAttr sessionAttr) {
        this.sessionAttr = sessionAttr;
    }

    @Override
    public <K> Message<K> newMessage() {
        SimpleMessage<K> msg = new SimpleMessage<K>();
        msg.setRequestAttr(requestAttr);
        msg.setSessionAttr(sessionAttr);
        return msg;
    }

}
