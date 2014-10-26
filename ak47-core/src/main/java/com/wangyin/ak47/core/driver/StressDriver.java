package com.wangyin.ak47.core.driver;

import com.wangyin.ak47.core.stress.BenchResult;
import com.wangyin.ak47.core.stress.Clip;
import com.wangyin.ak47.core.stress.Collector;


/**
 * 压力Driver
 * 
 * Example:
 *  TODO:
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public interface StressDriver<Q, R> extends Driver<Q, R> {
    
    public void loadClip(Clip<Q> clip);
    
    public void newConnection();
    
    public BenchResult bench(); 
    
    public BenchResult bench(int concurrency);
    
    public BenchResult bench(int concurrency, boolean keepalive);
    
    public BenchResult bench(int concurrency, boolean keepalive, int tpsLimit);
    
    public boolean isOver();
    
    public void hold();
    
    public void holdUntilFiredNum(long num);
    
    public void holdUntilSeconds(int seconds);
    
    public void setCollector(Collector<Q, R> collector);

    
    
    
    
    
}
