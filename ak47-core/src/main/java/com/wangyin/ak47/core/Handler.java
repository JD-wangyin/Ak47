package com.wangyin.ak47.core;


/**
 * PipeHandler (in AK47) and ChannelHandler (in Netty) is very similar, 
 * in fact, they use almost the same.
 * 
 * The relationship of O/I/Q/R :
 * 
 *                  O: Outbound POJO
 *                  I: Inbound POJO
 *                  Q: Request POJO
 *                  R: Response POJO
 *
 *   +--------+  O=Q                I=Q +--------+
 *   |        | ----------------------> |        |
 *   | Driver |                         |  Stub  |
 *   |        | <---------------------- |        |
 *   +--------+  I=R                O=R +--------+
 *
 * 
 * 
 * PipeHandler(in AK47) 与  ChannelHandler(in Netty) 非常相似，实际上在使用上它们几乎是一样的。
 * 
 * 在设计理念上，PipeHandler 稍有一些不同。
 * 
 * PipeHandler 是由 Pipe 这个抽象概念而来，既然 Pipe 是一个 双面人，
 * 那么 PipeHandler 也是需要具备双面性的， 一面是为Driver服务，另一面是为Stub服务。
 * 
 * 通常 PipeHandler 是成对出现的，它隐含了一种对称，但不是强制性的。
 * 
 * 注意： O/I/Q/R 4个的关系：
 * 
 * 
 * @author hannyu
 *
 * @param <O>           Outbound-POJO
 * @param <I>           Inbound-POJO
 */
public interface Handler<O, I> {
    

    public void doHandlerAdded(HandlerContext<O, I> ctx);
    public void doHandlerRemoved(HandlerContext<O, I> ctx);
    
    
    /**
     * Called once a send operation is made.
     * 
     * 当要发送消息时。
     * 
     * @param ctx
     * @param msg       "O" means Outbound-POJO, or Request-POJO if in Stub, or 
     *                  Response-POJO if in Driver.
     * @throws Exception
     */
    public void doSend(HandlerContext<O, I> ctx, Message<O> msg, Promise<O, I> promise) throws Exception;
    
    public void doDisconnect(HandlerContext<O, I> ctx, Promise<O, I> promise) throws Exception;
    
    //////////////////////////////////////////////////////////////////////////
    /////   Outbound / Inbound
    //////////////////////////////////////////////////////////////////////////
    
    /**
     * Invoked when the current {@link Pipe} has received a message from the
     * peer.
     * 
     * 当接收到消息时。
     * 
     * @param ctx
     * @param msg       "I" means Inbound-POJO, or Request-POJO if in Stub, or 
     *                  Response-POJO if in Driver.
     * @throws Exception
     */
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception;
    
    
    //Maybe using open/close better than connected/disconnected.
    /**
     * Called once a connection is open.
     * 
     * 当连接建立后。
     * 
     * @param ctx
     * @throws Exception
     */
    public void doConnected(HandlerContext<O, I> ctx) throws Exception;
    
    
    /**
     * Called once a connection is closed.
     * 
     * 当连接断开后。
     * 
     * @param ctx
     * @throws Exception
     */
    public void doDisconnected(HandlerContext<O, I> ctx) throws Exception;
    
    
    /**
     * Gets called if a Throwable was thrown.
     * 
     * 当发现异常时。
     * 
     * @param ctx
     * @param e
     * @throws Exception
     */
    public void doCaught(HandlerContext<O, I> ctx, Throwable cause) throws Exception;
    
    
    /**
     * The Channel of {@link HandlerContext} was registered with its {@link EventLoop}
     * 
     * 当channel注册到EventLoop中。
     * 
     * @param ctx
     * @throws Exception
     */
//    public void doRegistered(HandlerContext<O, I> ctx) throws Exception;
    
    
    /**
     * The Channel of {@link HandlerContext} was unregistered with its {@link EventLoop}
     * 
     * 当channel取消注册到EventLoop中。
     * 
     * @param ctx
     * @throws Exception
     */
//    public void doUnregistered(HandlerContext<O, I> ctx) throws Exception;
    
}
