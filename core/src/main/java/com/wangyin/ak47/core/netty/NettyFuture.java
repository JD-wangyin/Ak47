package com.wangyin.ak47.core.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.wangyin.ak47.core.Channel;
import com.wangyin.ak47.core.FutureListener;
import com.wangyin.ak47.core.Future;


public class NettyFuture<O, I> implements Future<O, I> {
    
    private ChannelFuture realFuture;
    private Channel<O, I> channel;
    
    
    public NettyFuture(Channel<O, I> channel, ChannelFuture realFuture){
        this.channel = channel;
        this.realFuture = realFuture;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return realFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return realFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return realFuture.isDone();
    }

    @Override
    public Channel<O, I> get() throws InterruptedException,
            ExecutionException {
        return channel;
    }

    @Override
    public Channel<O, I> get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return get();
    }


    @Override
    public void addListener(final FutureListener listener) {
        realFuture.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                listener.onComplete(channel);
            }
            
        });
    }
    
}
