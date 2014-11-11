package com.wangyin.ak47.core;



/**
 * 过滤某些消息，可用于处理心跳包之类。
 * 
 * @author hannyu
 *
 */
public interface Filter<Q, R> {

    public boolean filterReceivedInStub(HandlerContext<R, Q> ctx, Message<Q> msg);
    
    public boolean filterReceivedInDriver(HandlerContext<Q, R> ctx, Message<R> msg);
    
} 

