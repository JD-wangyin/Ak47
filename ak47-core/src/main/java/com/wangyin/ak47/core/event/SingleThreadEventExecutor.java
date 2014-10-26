package com.wangyin.ak47.core.event;


import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author hannyu
 *
 */
public class SingleThreadEventExecutor extends ThreadPoolExecutor implements Executor {

    public SingleThreadEventExecutor(int corePoolSize, int maximumPoolSize) {
        super(corePoolSize, maximumPoolSize, 30, TimeUnit.SECONDS, 
                new SynchronousQueue<Runnable>(), 
                new Ak47ThreadFactory());
    }

    

}
