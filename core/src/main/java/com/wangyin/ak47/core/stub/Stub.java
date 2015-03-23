package com.wangyin.ak47.core.stub;

import com.wangyin.ak47.core.handler.HandlerInitializer;

/**
 * Stub base
 * 
 * @author hannyu
 * 
 * @param <Q>
 * @param <R>
 */
public interface Stub<Q, R> {

    /**
     * 设置监听端口
     * 
     * @param port
     *            监听端口
     */
    public void setPort(int port);
    
    
    /**
     * 设置超时，单位毫秒
     * 
     * @param timeoutMillis
     */
//    public void setTimeoutMillis(int timeoutMillis);


    /**
     * 启动server，开启服务。
     * 
     * @throws Exception
     */
    public void start() throws Exception;

    /**
     * hold住，一直运行。
     * 
     * @throws Exception
     */
    public void hold() throws Exception;

    
    /**
     * 停止服务
     * 
     * @throws Exception
     */
    public void stop() throws Exception;

    /**
     * 关闭所有可能的服务，释放所有的资源，不可再用。
     */
    public void release();



    /**
     * add userInitializer
     * 
     * @param userInitializer
     */
    public void userInitializer(HandlerInitializer<R, Q> userInitializer);

}
