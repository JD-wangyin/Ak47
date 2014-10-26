package com.wangyin.ak47.core.handler;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;

public class LoggingInboundHandler<O, I> extends HandlerAdapter<O, I> {
    private static final Logger log = new Logger(LoggingInboundHandler.class);

    
//    @Override
//    public void doSend(HandlerContext<O, I> ctx, Message<O> msg) throws Exception {
////        log.info("\n====== send ======\n{}====== send ======\n", YmlUtil.obj2PrettyYml(msg.getPojo()));
//    }
//
//    
//    @Override
//    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception {
////        log.info("\n====== received ======\n{}====== received ======\n", YmlUtil.obj2PrettyYml(msg.getPojo()));
//    }


}
