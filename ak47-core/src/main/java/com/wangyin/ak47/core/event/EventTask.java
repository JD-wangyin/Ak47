package com.wangyin.ak47.core.event;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Channel;

public abstract class EventTask<O, I> implements Runnable {
    private static final Logger log = new Logger(EventTask.class); 
    
    private Channel<O, I> channel;
    
    public EventTask(Channel<O, I> channel){
        this.channel = channel;
    }
    
    @Override
    public void run(){
        try{
            doEvent(channel);
        }catch(Exception e){
            log.warn("{}.doEvent got an Exception.", this.getClass(), e);
        }
    }

    protected abstract void doEvent(Channel<O, I> channel) throws Exception;
    
}
