package com.wangyin.ak47.core.driver;



/**
 * Client简单实现
 * 所有方法默认都是阻塞的。
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public interface SimpleDriver<Q, R> extends Driver<Q, R> {
    
    /**
     * 发送数据，并阻塞，直到接收到返回数据。
     * 
     * @param q
     * @return
     * @throws Exception 
     */
    public R send(Q q) throws Exception;
    
    /**
     * 与send相同，接收完数据之后立即关闭连接。
     * 
     * @param q
     * @return
     * @throws Exception 
     */
    public R sendAndClose(Q q) throws Exception;
    
    /**
     * 关闭连接。
     * @throws Exception 
     */
    public void close() throws Exception;
    
    
    
    
    
    
    
    
}
