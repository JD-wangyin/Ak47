package com.wangyin.ak47.core.stress;


/**
 * 弹夹 Clip。
 * 做压力就像是拿着一把AK47连续射击，需要充足的子弹，弹夹就是子弹的来源。
 * 
 * @author wyhanyu
 *
 */
public interface Clip<Q> {

    /**
     * 装载子弹
     * @param bullet
     */
    public void put(Q bullet);
    
    
    /**
     * 取出子弹（准备发射）
     * 
     * 
     * @return null if Clip is empty.
     */
    public Bullet<Q> take();
    
    
    /**
     * 设置取弹模式
     * 
     * @param mode
     */
    public void setMode(ClipMode mode);
    
    
    /**
     * 设置最大装弹量，控制了发射的数量，实际能发射的数量一定小于这个数。
     * 
     * 0表示Long.MAX_VALUE，不能为负数。
     * 
     * @param capacity
     */
    public void capacity(long capacity);
    
    /**
     * 返回当前最大装弹量
     * 
     * @return
     */
    public long capacity();
    
    
    /**
     * 是否已经打空
     * 
     * @return
     */
    public boolean empty();
    
}
