/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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
