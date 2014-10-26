package com.wangyin.ak47.core;




/**
 * Context of PipeHandler, everything network operations/scheduling/cache 
 * and other functions, are used as the operating PipeHandlerContext entrance.
 * 
 * PipeHandler的上下文，一切网络操作、调度器、缓存等功能，都是用 PipeHandlerContext 作为操作入口。
 * 
 * @author hannyu
 *
 * @param <O>       Outbound POJO
 * @param <I>       Inbound POJO
 */
public interface HandlerContext<O, I> {
    
    
    /**
     * return name
     * 
     * @return
     */
    public String name();
    
    /**
     * return handler
     * 
     * @return
     */
    public Handler<O, I> handler();

    /**
     * chain
     * 
     * @return
     */
    public HandlerChain<O, I> chain();

    /**
     * channel
     * 
     * @return
     */
    public Channel<O, I> channel();
    
    
    /**
     * 发送数据
     * @param msg
     * @return 
     */
    public Future<O, I> send(O pojo);
    public Future<O, I> send(O pojo, Promise<O, I> promise);
    public Future<O, I> send(Message<O> msg);
    public Future<O, I> send(Message<O> msg, Promise<O, I> promise);
    
    /**
     * 断开连接
     * 
     * @return
     */
    public Future<O, I> disconnect() throws Exception;
    public Future<O, I> disconnect(Promise<O, I> promise) throws Exception;
    
    public void fireConnected();
    public void fireDisconnected();
    public void fireReceived(Message<I> msg);
    public void fireCaught(Throwable cause);
    
    public Scheduler scheduler();
    

}
