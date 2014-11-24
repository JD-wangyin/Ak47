package com.wangyin.ak47.core.handler;

import com.wangyin.ak47.core.message.SimpleRequest;
import com.wangyin.ak47.core.message.SimpleResponse;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Codec;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Promise;



public class CodecStubHandler<O, I> extends HandlerAdapter<O, I> {
    private static Logger log = new Logger(CodecStubHandler.class);
    
    
    private Codec<I, O> codec;
    
    public CodecStubHandler(Codec<I, O> codec){
        this.codec = codec;
    }

    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception {
        
        SimpleRequest<I> request = new SimpleRequest<I>(msg);
        codec.decodeRequest(msg.getBuffer(), request);
        if( request.hasPojo() ){
            ctx.fireReceived(msg);
        }else{
            log.warn("Interrupted for decodeRequest fail. ");
        }
        
    }

    @Override
    public void doSend(HandlerContext<O, I> ctx, Message<O> msg, 
            Promise<O, I> promise) throws Exception {
        
        SimpleResponse<O> response = new SimpleResponse<O>(msg);
        codec.encodeResponse(response, msg.getBuffer());
        
        if( msg.getBuffer().isReadable() ){
            ctx.send(msg, promise);
        }else{
            log.warn("Interrupted for encodeResponse fail.");
        }
        
    }

    
}
