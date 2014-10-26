package com.wangyin.ak47.core.stub;

import com.wangyin.ak47.core.stress.Clip;


/**
 * 压力Stub
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public interface StressStub<Q, R> extends Stub<Q, R> {
    
    public void loadClip(Clip<R> clip);
    
    public void setKeepAlive(boolean keepalive);
    
}
