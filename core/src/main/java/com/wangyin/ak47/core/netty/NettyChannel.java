package com.wangyin.ak47.core.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelPromise;

import java.net.InetSocketAddress;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Channel;
import com.wangyin.ak47.core.Future;
import com.wangyin.ak47.core.HandlerChain;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Promise;
import com.wangyin.ak47.core.Scheduler;
import com.wangyin.ak47.core.message.SimpleMessage;
import com.wangyin.ak47.core.util.SimpleScheduler;
import com.wangyin.ak47.core.util.SimpleSessionAttr;

/**
 * Channel的Netty实现
 * 
 * @author wyhanyu
 *
 */
public class NettyChannel<O, I> implements Channel<O, I> {
    private static final Logger log = new Logger(NettyChannel.class);
    
    // io.netty.channel.Channel
    private ChannelHandlerContext realCtx;
    private io.netty.channel.Channel realChannel;
    
    private HandlerChain<O, I> chain;
    private NettyUnsafe unsafe;
    private SimpleSessionAttr sessionAttr;

    public NettyChannel(io.netty.channel.Channel realChannel) {
        this.realChannel = realChannel; 
        chain = new HandlerChain<O, I>(this);
        unsafe = new NettyUnsafe(realChannel);
        sessionAttr = new SimpleSessionAttr();
    }
    

    public void setChannelHandlerContext(ChannelHandlerContext realCtx){
        this.realCtx = realCtx;
    }
    
    public ChannelHandlerContext getChannelHandlerContext(){
        return realCtx;
    }
    
    @Override
    public Unsafe<O, I> unsafe() {
        return unsafe;
    }
    
    @Override
    public HandlerChain<O, I> chain(){
        return chain;
    }
    
    @Override
    public Scheduler scheduler(){
        return new SimpleScheduler(realChannel.eventLoop());
    }
    

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) realChannel.remoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) realChannel.localAddress();
    }

    @Override
    public boolean isConnected() {
        return realChannel.isActive();
    }

    @Override
    public String id() {
        //FIXME 4.0
        //return realChannel.id().asLongText();
        return realChannel.toString();
    }
    
    @Override
    public Promise<O, I> newPromise(){
        DefaultChannelPromise realPromise = new DefaultChannelPromise(realChannel);
        NettyPromise<O, I> promise = new NettyPromise<O, I>(this, realPromise);
        return promise;
    }
    
    @Override
    public Message<O> newMessage(O pojo) {
        SimpleMessage<O> msg = new SimpleMessage<O>(pojo);
        msg.setSessionAttr(sessionAttr);
        msg.setRequestAttr(sessionAttr.newRequestAttr());
        return msg;
    }
    
    @Override
    public Future<O, I> send(Message<O> msg) throws Exception {
        log.debug("send().");
        
        Promise<O, I> promise = newPromise();
        chain.send(msg, promise);
        return promise;
    }
    
    @Override
    public Future<O, I> disconnect() throws Exception {
        log.debug("disconnect().");
        
        Promise<O, I> promise = newPromise();
        chain.disconnect(promise);
        return promise;
    }

    @Override
    public void fireConnected(){
        log.debug("fireConnected().");
        
        chain.fireConnected();
    }
    
    @Override
    public void fireReceived(Message<I> msg){
        log.debug("fireReceived().");
        
        SimpleMessage<I> msg0 = (SimpleMessage<I>) msg;
        if( msg0.hasBuffer() ){
            msg0.setSessionAttr(sessionAttr);
            msg0.setRequestAttr(sessionAttr.newRequestAttr());
            chain.fireReceived(msg0);
        }else{
            log.error("why fireReceived a NULL buffered msg?");
        }
    }
    
    @Override
    public void fireDisconnected(){
        log.debug("fireDisconnected().");
        
        chain.fireDisconnected();
    }
    
    @Override
    public void fireCaught(Throwable cause){
        log.debug("fireCaught().");
        
        chain.fireCaught(cause);
    }
    
    class NettyUnsafe implements Unsafe<O, I>{
        private io.netty.channel.Channel realChannel;
        public NettyUnsafe(io.netty.channel.Channel realChannel){
            this.realChannel = realChannel;
        }

        @Override
        public void send(Message<O> msg, Promise<O, I> promise) {
            NettyBuffer buf = (NettyBuffer) msg.getBuffer();
            byte[] bytes = new byte[buf.getByteBuf().readableBytes()];
            buf.getByteBuf().copy().readBytes(bytes);
            
            NettyPromise<O, I> np = (NettyPromise<O, I>) promise;
            realChannel.writeAndFlush(buf.getByteBuf(), np.channelPromise());
        }

        @Override
        public void disconnect(Promise<O, I> promise) {
            NettyPromise<O, I> np = (NettyPromise<O, I>) promise;
            realChannel.disconnect(np.channelPromise());
        }
        
    }

}
