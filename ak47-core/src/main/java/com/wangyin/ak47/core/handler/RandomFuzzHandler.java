package com.wangyin.ak47.core.handler;


import com.wangyin.ak47.common.RandomUtil;
import com.wangyin.ak47.common.SimpleFuzz;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.Promise;

/**
 * 
 * @author hannyu
 *
 * @param <O>
 * @param <I>
 */
public class RandomFuzzHandler<O, I> extends HandlerAdapter<O, I> {

	private int fuzzPermillage;
	private SimpleFuzz fuzz;

	public RandomFuzzHandler(int fuzzPermillage, int seed) {
		super();
		
		if( fuzzPermillage > 1000 || fuzzPermillage < 0){
            throw new IllegalArgumentException("Permillage must between 0 and 1000.");
        }
		this.fuzzPermillage = fuzzPermillage;
		this.fuzz = new SimpleFuzz(seed);
	}

	@Override
	public void doSend(HandlerContext<O, I> ctx, Message<O> msg,
            Promise<O, I> promise) throws Exception {
	    
	    if( msg.hasBuffer() ){
	        fuzz(msg.getBuffer());
	    }
	    
	    ctx.send(msg, promise);
	}
	
	
	private void fuzz(Buffer buf) {
	    int srclen = buf.readableBytes();
	    int readerIndex = buf.readerIndex();
	    
	    int fuzzNum = srclen * fuzzPermillage / 1000;
        for(int i=0; i<fuzzNum; i++){
            int pos = RandomUtil.nextInt(srclen) + readerIndex;
            byte dst = fuzz.fuzz(buf.getByte(pos));
            buf.setByte(pos, dst);
        }
	}
	
	
}
