package com.wangyin.ak47.core.stress;

import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.Message;

/**
 * 子弹
 * 
 * @author wyhanyu
 *
 * @param <Q>
 */
public interface Bullet<Q> extends Message<Q> {
    
    
    public boolean isReady();
    
    public Buffer copyBuffer();
    
    public void buffer(Buffer buf);
    
}
