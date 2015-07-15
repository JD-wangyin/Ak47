package com.wangyin.ak47.core.handler;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Promise;

public class LoggingTrafficHandler<O, I> extends HandlerAdapter<O, I> {
    private static final Logger log = new Logger(LoggingTrafficHandler.class);

    @Override
    public void doSend(HandlerContext<O, I> ctx, Message<O> msg, 
            Promise<O, I> promise) throws Exception {
        log.info("Send {} bytes to {}.", msg.getBuffer().readableBytes(), 
                ctx.channel().getRemoteAddress().toString());
        
        ctx.send(msg, promise);
    }

    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) 
            throws Exception {
        log.info("Received {} bytes from {}.", msg.getBuffer().readableBytes(), 
                ctx.channel().getRemoteAddress().toString());
        
        ctx.fireReceived(msg);
    }

}
