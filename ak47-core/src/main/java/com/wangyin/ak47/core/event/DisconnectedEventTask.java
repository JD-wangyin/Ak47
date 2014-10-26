package com.wangyin.ak47.core.event;


import com.wangyin.ak47.core.Channel;

public class DisconnectedEventTask<O, I> extends EventTask<O, I>{
    
    public DisconnectedEventTask(Channel<O, I> channel) {
        super(channel);
    }
    
    @Override
    protected void doEvent(Channel<O, I> channel) throws Exception {
        channel.fireDisconnected();
    }
    
}
