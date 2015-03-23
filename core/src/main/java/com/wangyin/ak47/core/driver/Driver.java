package com.wangyin.ak47.core.driver;

import com.wangyin.ak47.core.handler.HandlerInitializer;


/**
 * Driver base
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public interface Driver<Q, R> {
    
    
    public void setAddr(String addr);
    
    public void setPort(int port);
    

    /**
     * 关闭所有可能的服务，释放所有的资源，且Driver不可再用。
     */
    public void release();
    


    /**
     * add HandlerInitializer
     * 
     * @param userInitializer
     */
    public void userInitializer(HandlerInitializer<Q, R> userInitializer);
    
    
}
