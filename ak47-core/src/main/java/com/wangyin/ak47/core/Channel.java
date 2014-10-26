package com.wangyin.ak47.core;

import java.net.InetSocketAddress;



/**
 * A nexus for java.nio.channels.Channel, which is a nexus for I/O operations.
 * 
 * 作为java.nio.channels.Channel的弱代理，每个Channel对应一个Connection。
 * 
 * @author hannyu
 *
 */
public interface Channel<O, I> {

    
    /**
     * Get remote address, including ip and port.
     * 
     * 获取远端地址 ip + port
     * 
     * @return
     */
    public InetSocketAddress getRemoteAddress();
    
    
    /**
     * Get local address, including ip and port.
     * 
     * 获取本地地址 ip + port
     * 
     * @return
     */
    public InetSocketAddress getLocalAddress();
    
    
    /**
     * If connected and active.
     * 
     * 是否已连接成功（发起连接但还未连接成功不算）
     * 
     * 注：对应于 Netty Channel.isActive()
     * 
     * @return
     */
    public boolean isConnected();
    
    
    /**
     * Get the universal unique ID.
     * 
     * 获取唯一识别id
     * 
     * @return
     */
    public String id();

    
    public HandlerChain<O, I> chain();
    
    public Promise<O, I> newPromise();
    
    public Message<O> newMessage(O pojo);
    
    /**
     * 发送数据
     * @param msg
     * @return 
     * @throws Exception 
     */
    public Future<O, I> send(Message<O> msg) throws Exception;

    /**
     * 断开连接
     * 
     * @return
     * @throws Exception 
     */
    public Future<O, I> disconnect() throws Exception;
    

    public void fireConnected();
    public void fireReceived(Message<I> msg);
    public void fireDisconnected();
    public void fireCaught(Throwable cause);
    
    public Scheduler scheduler();

    Unsafe<O, I> unsafe();
    
    /**
     * <em>Unsafe</em> operations that should <em>never</em> be called from user-code. These methods
     * are only provided to implement the actual transport, and must be invoked from an I/O thread.
     * 
     * @author hannyu
     *
     * @param <O>
     * @param <I>
     */
    interface Unsafe<O, I> {
        void send(Message<O> msg, Promise<O, I> promise);
        void disconnect(Promise<O, I> promise);
    }
    
}
