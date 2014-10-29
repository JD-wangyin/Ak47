package com.wangyin.ak47.core.netty;


import io.netty.channel.ChannelPromise;

import com.wangyin.ak47.core.Channel;
import com.wangyin.ak47.core.Promise;


public class NettyPromise<O, I> extends NettyFuture<O, I> implements Promise<O, I> {

    private ChannelPromise realPromise;
    
    public NettyPromise(Channel<O, I> channel, ChannelPromise realPromise) {
        super(channel, realPromise);
        this.realPromise = realPromise;
    }
    
    public ChannelPromise channelPromise(){
        return realPromise;
    }

    @Override
    public Promise<O, I> setSuccess() {
        realPromise.setSuccess();
        return this;
    }

    @Override
    public boolean trySuccess() {
        return realPromise.trySuccess();
    }

    @Override
    public Promise<O, I> setFailure(Throwable cause) {
        realPromise.setFailure(cause);
        return this;
    }

    @Override
    public boolean tryFailure(Throwable cause) {
        return realPromise.tryFailure(cause);
    }

    @Override
    public Promise<O, I> await() throws InterruptedException {
        realPromise.await();
        return this;
    }

    @Override
    public Promise<O, I> awaitUninterruptibly() {
        realPromise.awaitUninterruptibly();
        return this;
    }

    @Override
    public Promise<O, I> sync() throws InterruptedException {
        realPromise.sync();
        return this;
    }

    @Override
    public Promise<O, I> syncUninterruptibly() {
        realPromise.syncUninterruptibly();
        return this;
    }

    
}
