package com.wangyin.ak47.core.handler;


import java.util.concurrent.TimeUnit;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Promise;


public class NetworkDelayHandler<O, I> extends HandlerAdapter<O, I>{
    private static final Logger log = new Logger(NetworkDelayHandler.class);
	
	private long delayMillis;
	
	
	
	public NetworkDelayHandler(long delayMillis) {
		this.delayMillis = delayMillis;
	}

	
	@Override
	public void doSend(final HandlerContext<O, I> ctx, final Message<O> msg, 
            final Promise<O, I> promise) throws Exception {

	    if( delayMillis > 0 ){
	        log.debug("Need delayMillis: {}", delayMillis);
	        
	        ctx.scheduler().schedule(new Runnable(){
                @Override
                public void run() {
                    ctx.send(msg, promise);
                }
	            
	        }, delayMillis, TimeUnit.MILLISECONDS);
	    }else{
	        ctx.send(msg, promise);
	    }
	    
	}
	
	
}
