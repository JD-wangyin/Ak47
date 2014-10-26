package com.wangyin.ak47.core.handler;

import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Promise;

/**
 * 放在最外层，专门计算响应时间。
 * 
 * @author wyhanyu
 *
 * @param <O>
 * @param <I>
 */
public class CalculateResponseTime<O, I> extends HandlerAdapter<O, I> {

    public static final String KEY_CRT_BEGIN_MILLIS = "key_crt_begin_millis";
    public static final String KEY_CRT_END_MILLIS = "key_crt_end_millis";
    public static final String KEY_CRT_TAKE_MILLIS = "key_crt_take_millis";
    
    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception {
        //TODO
//        long end = System.currentTimeMillis();
//        long begin = (Long) ctx.getAttr(KEY_CRT_BEGIN_MILLIS);
//        int take = (int) (end-begin);
//        ctx.setAttr(KEY_CRT_END_MILLIS, end);
//        ctx.setAttr(KEY_CRT_TAKE_MILLIS, take);
        
        ctx.fireReceived(msg);
    }
    
    @Override
    public void doSend(HandlerContext<O, I> ctx, Message<O> msg,
            Promise<O, I> promise) throws Exception {
        //TODO
//        ctx.setAttr(KEY_CRT_BEGIN_MILLIS, System.currentTimeMillis());
        
        ctx.send(msg, promise);
    }
    
    
    
}
