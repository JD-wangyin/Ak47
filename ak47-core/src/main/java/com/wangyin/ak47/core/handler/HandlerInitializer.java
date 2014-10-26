package com.wangyin.ak47.core.handler;


import com.wangyin.ak47.core.HandlerChain;
import com.wangyin.ak47.core.HandlerContext;



public abstract class HandlerInitializer<O, I> extends HandlerAdapter<O, I> {
    

    public abstract void initHandler(HandlerChain<O, I> chain);
    
    @Override
    public void doHandlerAdded(HandlerContext<O, I> ctx) {
        HandlerChain<O, I> chain = ctx.chain();
        
        initHandler(chain);
        chain.remove(this);
    }
    
    
}
