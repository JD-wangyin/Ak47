package com.wangyin.ak47.core.event;


import com.wangyin.ak47.core.Channel;
import com.wangyin.ak47.core.Message;

public class ReceivedEventTask<O, I> extends EventTask<O, I>{

    private Message<I> msg;
    
    public ReceivedEventTask(Channel<O, I> channel, Message<I> msg){
        super(channel);
        this.msg = msg;
    }
    
    @Override
    protected void doEvent(Channel<O, I> channel) throws Exception {
        channel.fireReceived(msg);
    }

}
