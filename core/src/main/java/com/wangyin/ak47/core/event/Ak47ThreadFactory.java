package com.wangyin.ak47.core.event;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class Ak47ThreadFactory implements ThreadFactory {
    
    private static final String THREAD_NAME_PREFIX_PREFIX = "ak47-thread-";
    
    private static final AtomicInteger POOL_NUM = new AtomicInteger(1);
    private AtomicInteger threadNum = new AtomicInteger(1);
    private String prefix;
    
    public Ak47ThreadFactory(){
        prefix = THREAD_NAME_PREFIX_PREFIX + POOL_NUM.getAndIncrement() + "-";
    }
    
    @Override
    public Thread newThread(Runnable runnable){
        String name = prefix + threadNum.getAndIncrement();
        Thread thread = new Thread(runnable, name);
        return thread;
    }

}
