package com.wangyin.ak47.core.stub;


import com.wangyin.ak47.core.Service;


/**
 * 简单Stub，提供Service机制
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public interface SimpleStub<Q, R> extends Stub<Q, R> {
    
    /**
     * 添加Service
     * 
     * @param name
     * @param service
     */
    public void addService(String name, Service<Q, R> service);

    /**
     * 删除Service
     * @param name
     */
    public void removeService(String name);
    
}
