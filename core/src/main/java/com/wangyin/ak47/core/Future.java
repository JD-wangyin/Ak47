package com.wangyin.ak47.core;



/**
 * A Future represents the result of an asynchronous computation. 
 * See {@link java.util.concurrent.Future}.
 * 
 * @author hannyu
 *
 */
public interface Future<O, I> extends java.util.concurrent.Future<Channel<O, I>> {

    /**
     * Add a FutureListener.
     * 
     * @param listener
     */
    public void addListener(FutureListener listener);
    
    // may not need
    // public void removeListener(FutureListener listener);
    
}
