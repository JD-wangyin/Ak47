package com.wangyin.ak47.core.handler;

import com.wangyin.ak47.core.message.SimpleResponse;
import com.wangyin.ak47.core.stress.Bullet;
import com.wangyin.ak47.core.stress.Clip;
import com.wangyin.ak47.core.stress.Collector;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.FutureListener;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.Future;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.exception.Ak47Exception;


/**
 * TODO reconstruction
 * 
 * @author wyhanyu
 *
 */
public class StressStubHandler<O, I> extends HandlerAdapter<O, I> {
    private static Logger log = new Logger(StressStubHandler.class);
    
    private Collector<O, I> collector;
    public void setCollector(Collector<O, I> collector){
        this.collector = collector;
    }
    
    private Clip<O> clip;
    public void loadClip(Clip<O> clip){
        this.clip = clip;
    }
    
    private boolean keepalive;
    public void setKeepAlive(boolean keepalive){
        this.keepalive = keepalive;
    }
    
    private Pipe<I, O> pipe;
    public StressStubHandler(Pipe<I, O> pipe){
        this.pipe = pipe;
    }
    
    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception {
        if( null != collector ){
            //TODO
        }
        
        Bullet<O> bullet = clip.take();
        
        if( bullet == null ){
            log.info("Clip is empty. Close connection.");
            ctx.disconnect();
            return;
        }
        
        if( ! bullet.isReady() ){
            // make fired
            synchronized(bullet){
                if( ! bullet.isReady() ){
                    Message<O> newmsg = msg.newMessage();
                    SimpleResponse<O> res = new SimpleResponse<O>(newmsg);
                    res.pojo(bullet.getPojo());
                    Buffer buf = newmsg.getBuffer();
                    pipe.encodeResponse(res, buf);
                    
                    if( ! bullet.isReady() ){
                        log.error("Fire bullet, but encodeResponse fail.");
                        throw new Ak47Exception("Fire bullet, but encodeResponse fail.");
                    }
                    
                }
            }
        }
        
        Future<O, I> future = ctx.send(bullet);
        if( ! keepalive ){
            future.addListener(FutureListener.CLOSE_ON_COMPLETE);
        }
    }
    
    
}



