package com.wangyin.ak47.core.stress;

import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.message.SimpleMessage;

/**
 * 子弹
 * 
 * @author wyhanyu
 *
 * @param <Q>
 */
public class SimpleBullet<Q> extends SimpleMessage<Q> implements Bullet<Q> {

    public SimpleBullet(Q pojo){
        super(pojo);
    }
    
    @Override
    public boolean isReady() {
        return hasBuffer() && buffer.isReadable();
    }

    @Override
    public Buffer copyBuffer() {
        // NOT copy()
        return getBuffer().duplicate();
    }
    
    @Override
    public void buffer(Buffer buf){
        super.buffer = buf.copy();
    }
    
}
