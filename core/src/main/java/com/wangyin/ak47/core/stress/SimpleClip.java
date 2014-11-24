package com.wangyin.ak47.core.stress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.wangyin.ak47.core.stress.ClipMode;
import com.wangyin.ak47.common.RandomUtil;
import com.wangyin.ak47.core.exception.Ak47RuntimeException;

/**
 * 弹夹的一个简单实现。提供多种模式：
 *  - DEFAULT           按装弹顺序取出，子弹总会打光的
 *  - REPEAT            重复取，子弹无限
 *  - REPEAT_ONE        重复一颗子弹（第一颗），子弹无限
 *  - SHUFFLE           随机取子弹，子弹无限
 *  
 *  
 * @author wyhanyu
 *
 * @param <Q>
 */
public class SimpleClip<Q> implements Clip<Q>{

    private List<Bullet<Q>> bullets = new ArrayList<Bullet<Q>>();
    private ClipMode mode = ClipMode.DEFAULT;
    private AtomicLong firedNum = new AtomicLong(0);
    
    private long capacity = Long.MAX_VALUE;
    

    @Override
    public void setMode(ClipMode mode) {
        this.mode = mode;
    }


    @Override
    public void capacity(long capacity) {
        if( capacity > 0 ){
            this.capacity = capacity;
        }else if( capacity < 0 ){
            throw new Ak47RuntimeException("Capacity should >= 0.");
        }
    }

    @Override
    public long capacity() {
        return capacity;
    }

    @Override
    public boolean empty() {
        return (firedNum.get() >= capacity);
    }

    
    @Override
    public void put(Q pojo) {
        Bullet<Q> bullet = new SimpleBullet<Q>(pojo);
        bullets.add(bullet);
    }

    @Override
    public Bullet<Q> take() {
        if( bullets.size() == 0 ){
            throw new Ak47RuntimeException("Have NOT any bullet.");
        }
        
        long i = firedNum.getAndIncrement();
        
        if( i >= capacity ){
            // not over capacity
            return null;
        }
        
        switch(mode){
        case DEFAULT:
            if( i < bullets.size() ){
                return bullets.get((int)i);
            }else{
                return null;
            }
        case REPEAT:
            return bullets.get((int)i%bullets.size());
        case REPEAT_ONE:
            return bullets.get(0);
        case SHUFFLE:
            return bullets.get( RandomUtil.nextInt(bullets.size()) );
        default:
            throw new Ak47RuntimeException("Unrecognized ClipMode.");
        }
        
    }

}