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
    
    // How many CPU ?
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    
    public static final int DEFAULT_CORE_POOL_SIZE = CPU_NUM;
    public static final int DEFAULT_MAX_POOL_SIZE = CPU_NUM * 4;
    
    private static final int KEEP_ALIVE_TIME = 30;
    
    /**
     * Creates a new ThreadPoolExecutor with the default pool size.
     */
    public SingleThreadEventExecutor(){
        this(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE);
    }
    
    /**
     * Creates a new ThreadPoolExecutor with the given initial parameters and 
     * default rejected execution handler.
     * 
     * @param corePoolSize          the number of threads to keep in the pool, 
     *                              even if they are idle, unless allowCoreThreadTimeOut is set
     * @param maximumPoolSize       maximumPoolSize the maximum number of threads to allow in the
     *                              pool
     */
    public SingleThreadEventExecutor(int corePoolSize, int maximumPoolSize) {
        super(corePoolSize, maximumPoolSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS, 
                new SynchronousQueue<Runnable>(), 
                new Ak47ThreadFactory());
    }

    

}
