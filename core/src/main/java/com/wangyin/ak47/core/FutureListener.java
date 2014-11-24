package com.wangyin.ak47.core;

import java.util.EventListener;


/**
 * Listens to the result of a {@link Future}.
 * The result of the asynchronous I/O operation will be notified.
 * 
 * @author hannyu
 *
 */
public interface FutureListener extends EventListener {

    
    /**
     * Invoked when the operation associated with the {@link Future} has been completed.
     * 
     * @param ctx
     * @throws Exception 
     */
    public <O, I> void onComplete(Channel<O, I> channel) throws Exception;

    
    
    /**
     * Close the channel when onComplete invoked.
     * 
     * 
     */
    public static FutureListener CLOSE_ON_COMPLETE = new FutureListener(){
        @Override
        public <O, I> void onComplete(Channel<O, I> channel) throws Exception {
            channel.disconnect();
        }

    };
    
}
