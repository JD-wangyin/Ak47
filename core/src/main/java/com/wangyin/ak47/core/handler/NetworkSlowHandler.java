package com.wangyin.ak47.core.handler;


import java.util.concurrent.TimeUnit;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.Promise;
import com.wangyin.ak47.core.message.SimpleMessage;

/**
 * 
 * @author wyhubingyin
 */
public class NetworkSlowHandler<O, I>  extends HandlerAdapter<O, I> {
    private static final Logger log = new Logger(NetworkSlowHandler.class);
    
    // 100ms
    private static final int intervalMillis = 100;
	
	private int sendSpeedLimit;


	/**
	 * 
	 * @param sendSpeedLimit send how many bytes per second
	 */
	public NetworkSlowHandler(int sendSpeedLimit) {
	    if( sendSpeedLimit <= 0 ){
	        throw new IllegalArgumentException("sendSpeedLimit should > 0.");
	    }
	    this.sendSpeedLimit = sendSpeedLimit;
	}


	@Override
	public void doSend(final HandlerContext<O, I> ctx, final Message<O> msg,
            final Promise<O, I> promise) throws Exception {
		
	    Buffer buf = msg.getBuffer();
	    int readerIndex = buf.readerIndex();
	    int buflen = buf.readableBytes();
	    int onelen = sendSpeedLimit * intervalMillis / 1000;
	    
	    if( buflen > onelen ){ 
	        Buffer onebuf = buf.slice(readerIndex, onelen);
	        buf.readerIndex(readerIndex + onelen);
	        SimpleMessage<O> onemsg = new SimpleMessage<O>(onebuf);
	        
	        ctx.scheduler().schedule(new Runnable(){
                @Override
                public void run() {
                    try {
                        NetworkSlowHandler.this.doSend(ctx, msg, promise);
                    } catch (Exception e) {
                        log.error("Scheduler doSend caught an exception.", e);
                    }
                }
            }, intervalMillis, TimeUnit.MILLISECONDS);
	        
	        ctx.send(onemsg); // no promise
	    }else{
	        ctx.send(msg, promise);
	    }
	    
	}
}
