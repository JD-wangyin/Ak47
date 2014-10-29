package com.wangyin.ak47.core.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;



public class BlockingQueueDriverHandler<O, I> extends HandlerAdapter<O, I> {
    private static Logger log = new Logger(BlockingQueueDriverHandler.class);
    
    private HandlerContext<O, I> ctx;
    private BlockingQueue<I> blockingQueue;
    private volatile O candidate;
    
    public BlockingQueueDriverHandler(){
        blockingQueue = new LinkedBlockingQueue<I>();
    }
    
    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg){
        log.debug("doReceived(): Got it.");
        
        blockingQueue.add(msg.getPojo());
        ctx.fireReceived(msg);
    }
    
    @Override
    public void doConnected(HandlerContext<O, I> ctx) throws Exception {
        this.ctx = ctx;
        synchronized(this){
            if( null == candidate ){
                log.debug("doConnected(): NO candidate to send.");
            }else{
                log.debug("doConnected(): Send a candidate.");
                ctx.send(candidate);
            }
        }
        
        ctx.fireConnected();
    }
    
    /**
     * 必须在connect之后
     * @param pojo
     */
    public void send(O pojo) throws Exception {
        if( null == ctx ){
            synchronized(this){
                if( null == candidate ){
                    candidate = pojo;
                    log.debug("send(): Before connected, hold.");
                }else{
                    log.error("send(): Connection is not established.");
                }
            }
        }else{
            ctx.send(pojo);
        }
    }
    
    public I recv() throws InterruptedException{
        log.debug("recv(): Wait for receiving one.");
        return blockingQueue.take();
    }


}
