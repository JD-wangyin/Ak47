package com.wangyin.ak47.core.handler;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Filter;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;


public class FilterStubHandler<O, I> extends HandlerAdapter<O, I> {
    private static final Logger log = new Logger(FilterStubHandler.class);
    
    private Filter<I, O> filter;
    public FilterStubHandler(Filter<I, O> filter){
        this.filter = filter;
    }

    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception {
        if( filter.filterReceivedInStub(ctx, msg) ){
            // do nothing
            log.debug("Filtered #{}.", msg.getRequestAttr().order());
        }else{
            ctx.fireReceived(msg);
        }
    }

}
