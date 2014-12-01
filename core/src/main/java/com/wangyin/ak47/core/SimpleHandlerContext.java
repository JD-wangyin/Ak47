package com.wangyin.ak47.core;

import com.wangyin.ak47.common.Logger;

public class SimpleHandlerContext<O, I> implements HandlerContext<O, I>{

    private static final Logger log = new Logger(SimpleHandlerContext.class);
    
    volatile SimpleHandlerContext<O, I> next;
    volatile SimpleHandlerContext<O, I> prev;
    
    private String name;
    private Handler<O, I> handler;
    private HandlerChain<O, I> chain;
    private Channel<O, I> channel;
    
    public SimpleHandlerContext(HandlerChain<O, I> chain, Channel<O, I> channel, String name, Handler<O, I> handler){
        this.chain = chain;
        this.channel = channel;
        this.name = name;
        this.handler = handler;
    }
    
    @Override
    public String name(){
        return name;
    }
    
    @Override
    public Handler<O, I> handler(){
        return handler;
    }
    
    @Override
    public HandlerChain<O, I> chain(){
        return chain;
    }
    
    @Override
    public Channel<O, I> channel(){
        return channel;
    }
    
    @Override
    public Future<O, I> send(O pojo) {
        return send(pojo, channel.newPromise());
    }
    
    @Override
    public Future<O, I> send(O pojo, Promise<O, I> promise) {
        return send(channel.newMessage(pojo), promise);
    }
    
    @Override
    public Future<O, I> send(Message<O> msg) {
        return send(msg, channel.newPromise());
    }
    
    @Override
    public Future<O, I> send(Message<O> msg, Promise<O, I> promise) {
        try{
            prev.handler().doSend(prev, msg, promise);
        }catch(Exception e){
            notifyOutboundException(e, promise);
        }
        return promise;
    }
    
    @Override
    public Future<O, I> disconnect() throws Exception {
        return disconnect(channel.newPromise());
    }
    
    @Override
    public Future<O, I> disconnect(Promise<O, I> promise) {
        try{
            prev.handler().doDisconnect(prev, promise);
        }catch(Exception e){
            notifyOutboundException(e, promise);
        }
        return promise;
    }
    
    
    @Override
    public void fireConnected() {
        try {
            next.handler().doConnected(next);
        } catch (Exception e) {
            notifyInboundException(next, e);
        }
    }
    
    @Override
    public void fireDisconnected() {
        try {
            next.handler().doDisconnected(next);
        } catch (Exception e) {
            notifyInboundException(next, e);
        }
    }
    
    @Override
    public void fireReceived(Message<I> msg) {
        try {
            next.handler().doReceived(next, msg);
        } catch (Exception e) {
            notifyInboundException(next, e);
        }
    }
    
    
    @Override
    public void fireCaught(Throwable cause){
        try {
            next.handler().doCaught(next, cause);
        } catch (Exception e) {
            log.warn("An exception was thrown by a user handler's doCaught() method:", e);
            log.warn(".. and the cause of the doCaught() was:", cause);
        }
    }
    
    @Override
    public Scheduler scheduler() {
        return channel.scheduler();
    }
    
    
    /**
     * 
     * @param ctx
     * @param cause
     */
    private void notifyInboundException(HandlerContext<O, I> ctx, Throwable cause) {
        try {
            next.handler().doCaught(next, cause);
        } catch (Exception e) {
            log.warn("An exception was thrown by a user handler's doCaught() method:", e);
            log.warn(".. and the cause of the doCaught() was:", cause);
        }
    }
    
    private void notifyOutboundException(Throwable cause, Promise<O, I> promise) {
        log.error("An exception of outbound event is fired.", cause);
        if( !promise.tryFailure(cause) ){
            log.error("Failed to fail the promise because it's done already: {}", promise, cause);
        }
    }
}
