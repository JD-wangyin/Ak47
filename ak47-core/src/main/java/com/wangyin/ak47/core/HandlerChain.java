package com.wangyin.ak47.core;


import com.wangyin.ak47.common.Logger;


/**
 * Handler Chain
 * 
 * @author hannyu
 *
 * @param <O>
 * @param <I>
 */
public class HandlerChain<O, I> {
    private static final Logger log = new Logger(HandlerChain.class);

    private Channel<O, I> channel;
    
	private SimpleHandlerContext<O, I> head;
	private SimpleHandlerContext<O, I> tail;

	public HandlerChain(Channel<O, I> channel) {
	    this.channel = channel;
	    
	    head = new HeadContext<O, I>(this, channel);
	    tail = new TailContext<O, I>(this, channel);
	    head.next = tail;
	    tail.prev = head;
	}
	
	
    
	/**
	 * 添加到最后
	 * 
	 * addLast(c):   head->a->b->tail  ==>  head->a->b->c->tail
	 * 
	 * 
	 * @param name     Handler名
	 * @param handler  添加的Handler
	 * @return
	 */
	public HandlerChain<O, I> addLast(String name, Handler<O, I> handler) {
	    log.debug("HandlerChain addLast {}.", name);
	    
	    SimpleHandlerContext<O, I> ctx = new SimpleHandlerContext<O, I>(this, channel, name, handler);
	    ctx.prev = tail.prev;
	    tail.prev = ctx;
	    
	    ctx.prev.next = ctx;
	    ctx.next = tail;
		
	    callHandlerAdded(ctx);
		return this;
	}
	
	/**
	 * 添加到开始
	 * 
	 * addFirst(c):   head->a->b->tail  ==>  head->c->a->b->tail
	 * 
	 * @param name
	 * @param handler
	 * @return
	 */
	public HandlerChain<O, I> addFirst(String name, Handler<O, I> handler) {
        log.debug("HandlerChain addFirst {}.", name);
        
        SimpleHandlerContext<O, I> ctx = new SimpleHandlerContext<O, I>(this, channel, name, handler);
        ctx.next = head.next;
        head.next = ctx;
        
        ctx.next.prev = ctx;
        ctx.prev = head;
        
        callHandlerAdded(ctx);
        return this;
    }
	
	/**
	 * 删除handler
	 * 
	 * remove(b):  head->a->b->tail  ===>  head->a->tail
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerChain<O, I> remove(Handler<O, I> handler){
	    log.debug("HandlerChain remove one.");
	    
	    SimpleHandlerContext<O, I> tmp = head.next;
	    SimpleHandlerContext<O, I> ctx = null;
	    while( tmp != tail ){
	        if( tmp.handler() == handler ){
	            ctx = tmp;
	            break;
	        }
	        tmp = tmp.next;
	    }
	    
	    if( ctx != null ){
	        ctx.prev.next = ctx.next;
	        ctx.next.prev = ctx.prev;
	        
	        callHandlerRemoved(ctx);
	    }
	    
	    return this;
	}
	
	
	private void callHandlerAdded(HandlerContext<O, I> ctx){
	    //FIXME maybe in executor()
	    ctx.handler().doHandlerAdded(ctx);
	}
	private void callHandlerRemoved(HandlerContext<O, I> ctx){
        //FIXME maybe in executor()
        ctx.handler().doHandlerRemoved(ctx);
    }
	

	public HandlerChain<O, I> replace(String name, Handler<O, I> handler) {
	    // TODO
	    return this;
	}
	

    /**
     * Inbound: head->next->next->...->tail
     * Outbound: tail->prev->prev->...->head
     * 
     * @author hannyu
     *
     * @param <O>
     * @param <I>
     */
    static final class HeadContext<O, I> extends SimpleHandlerContext<O, I> implements Handler<O, I> {
        
        public HeadContext(HandlerChain<O, I> chain, Channel<O, I> channel){
            super(chain, channel, "HeadContext", null);
        }
        
        @Override
        public Handler<O, I> handler() {
            return this;
        }

        @Override
        public void doSend(HandlerContext<O, I> ctx, Message<O> msg,
                Promise<O, I> promise) {
            log.debug("HeadContext doSend().");
            
            this.channel().unsafe().send(msg, promise);
        }

        @Override
        public void doDisconnect(HandlerContext<O, I> ctx, Promise<O, I> promise) {
            log.debug("HeadContext doDisconnect().");
            
            this.channel().unsafe().disconnect(promise);
        }
        
        
        @Override
        public void doConnected(HandlerContext<O, I> ctx) throws Exception {
            ctx.fireConnected();
        }
        

        @Override
        public void doReceived(HandlerContext<O, I> ctx, Message<I> msg)
                throws Exception {
            ctx.fireReceived(msg);
        }


        @Override
        public void doDisconnected(HandlerContext<O, I> ctx) throws Exception {
            ctx.fireDisconnected();
        }

        @Override
        public void doCaught(HandlerContext<O, I> ctx, Throwable cause)
                throws Exception {
            ctx.fireCaught(cause);
        }

        @Override
        public void doHandlerAdded(HandlerContext<O, I> ctx) {
            // do nothing.
        }

        @Override
        public void doHandlerRemoved(HandlerContext<O, I> ctx) {
            // do nothing.
        }
    }
    
    /**
     * Outbound: head->next->next->...->tail
     * Inbound: tail->prev->prev->...->head
     * 
     * @author hannyu
     *
     * @param <O>
     * @param <I>
     */
    static final class TailContext<O, I> extends SimpleHandlerContext<O, I> implements Handler<O, I> {

        public TailContext(HandlerChain<O, I> chain, Channel<O, I> channel){
            super(chain, channel, "TailContext", null);
        }
        
        @Override
        public Handler<O, I> handler() {
            return this;
        }

        @Override
        public void doSend(HandlerContext<O, I> ctx, Message<O> msg,
                Promise<O, I> promise) throws Exception {
            ctx.send(msg, promise);
        }

        @Override
        public void doDisconnect(HandlerContext<O, I> ctx, Promise<O, I> promise) throws Exception {
            ctx.disconnect(promise);
        }
        
        @Override
        public void doConnected(HandlerContext<O, I> ctx) throws Exception {
            // do nothing.
            log.debug("TailContext doConnected().");
        }
        
        @Override
        public void doReceived(HandlerContext<O, I> ctx, Message<I> msg)
                throws Exception {
            // do nothing.
            log.debug("TailContext doReceived().");
        }

        @Override
        public void doDisconnected(HandlerContext<O, I> ctx) throws Exception {
            // do nothing.
            log.debug("TailContext doDisconnected().");
        }

        @Override
        public void doCaught(HandlerContext<O, I> ctx, Throwable cause)
                throws Exception {
            log.warn("An doCaught() event was fired, and it reached at the tail of the chain.", cause);
        }

        @Override
        public void doHandlerAdded(HandlerContext<O, I> ctx) {
            // do nothing.
        }

        @Override
        public void doHandlerRemoved(HandlerContext<O, I> ctx) {
            // do nothing.
        }
        
    }


    public void send(Message<O> msg, Promise<O, I> promise) throws Exception {
        tail.send(msg, promise);
    }

    public void disconnect(Promise<O, I> promise) throws Exception {
        tail.disconnect(promise);
    }

    public void fireConnected() {
        head.fireConnected();
    }
    public void fireReceived(Message<I> msg){
        head.fireReceived(msg);
    }
    public void fireDisconnected(){
        head.fireDisconnected();
    }
    public void fireCaught(Throwable cause){
        head.fireCaught(cause);
    }
}
