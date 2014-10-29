package com.wangyin.ak47.core.netty;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.Executor;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Spliter;
import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.netty.NettyBuffer;
import com.wangyin.ak47.core.message.SimpleMessage;
import com.wangyin.ak47.core.event.ConnectedEventTask;
import com.wangyin.ak47.core.event.DisconnectedEventTask;
import com.wangyin.ak47.core.event.ReceivedEventTask;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;


/**
 * 连接Netty和AK47
 * 
 * @author wyhanyu
 *
 * @param <O>   Outbound
 * @param <I>   Inbound
 */
public class Netty4ChannelHandlerAdapter<O, I> implements ChannelInboundHandler, ChannelOutboundHandler {
    private static final Logger log = new Logger(Netty4ChannelHandlerAdapter.class);
    
    private NettyChannel<O, I> channel;
//    private ChannelHandlerContext realCtx;
    private Spliter spliter;
    private Executor executor;
    
    public Netty4ChannelHandlerAdapter(NettyChannel<O, I> channel, 
            Spliter spliter, Executor executor){
        this.channel = channel;
        this.spliter = spliter;
        this.executor = executor;
    }
    
    
    @Override
    public void handlerAdded(ChannelHandlerContext nettyctx) throws Exception {
        // do nothing
    }

    
    @Override
    public void handlerRemoved(ChannelHandlerContext nettyctx) throws Exception {
        // do nothing
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext nettyctx, Throwable cause)
            throws Exception {
        channel.fireCaught(cause);
    }
    

    @Override
    public void channelRegistered(ChannelHandlerContext nettyctx) throws Exception {
        nettyctx.fireChannelRegistered();
    }
    
    @Override
    public void deregister(ChannelHandlerContext nettyctx, ChannelPromise promise)
            throws Exception {
        nettyctx.deregister(promise);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext nettyctx) throws Exception {
        nettyctx.fireChannelUnregistered();
    }

    
    @Override
    public void channelActive(ChannelHandlerContext nettyctx) throws Exception {
        ConnectedEventTask<O, I> task = new ConnectedEventTask<O, I>(channel);
        executor.execute(task);
    }

    @Override
    public void channelInactive(ChannelHandlerContext nettyctx) throws Exception {
        DisconnectedEventTask<O, I> task = new DisconnectedEventTask<O, I>(channel);
        executor.execute(task);
    }

    
    private NettyBuffer tempbuf;
    @Override
    public void channelRead(ChannelHandlerContext nettyctx, Object msg)
            throws Exception {
        
        if( !(msg instanceof ByteBuf) ){
            log.error("Why channelRead get a NO ByteBuf?");
            nettyctx.fireChannelRead(msg);
            return;
        }
        
        ByteBuf bb = (ByteBuf) msg;
        if( null == tempbuf ){
            tempbuf = new NettyBuffer(bb);
        }else{
            tempbuf.writeBytes(bb);
            bb.release();
        }
        
        List<Buffer> bufs = spliter.split(tempbuf);
        int bufsize = bufs.size();
        log.debug("split into {}.", bufsize);
        
        for(int i=0; i<bufsize; i++){
            SimpleMessage<I> newmsg = new SimpleMessage<I>(bufs.get(i));
            ReceivedEventTask<O, I> task = new ReceivedEventTask<O, I>(channel, newmsg);
            executor.execute(task);
        }
        
        
        if( !tempbuf.isReadable() ){
            tempbuf.release();
            tempbuf = null;
        }
        
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext nettyctx) throws Exception {
        nettyctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext nettyctx, Object evt)
            throws Exception {
        nettyctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext nettyctx)
            throws Exception {
        nettyctx.fireChannelWritabilityChanged();
    }

    @Override
    public void bind(ChannelHandlerContext nettyctx, SocketAddress localAddress,
            ChannelPromise promise) throws Exception {
        nettyctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext nettyctx, SocketAddress remoteAddress,
            SocketAddress localAddress, ChannelPromise promise)
            throws Exception {
        nettyctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext nettyctx, ChannelPromise promise)
            throws Exception {
        nettyctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext nettyctx, ChannelPromise promise)
            throws Exception {
        nettyctx.close(promise);
    }

    @Override
    public void read(ChannelHandlerContext nettyctx) throws Exception {
        nettyctx.read();
    }

    
    @Override
    public void write(ChannelHandlerContext nettyctx, Object msg,
            ChannelPromise promise) throws Exception {
        nettyctx.write(msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext nettyctx) throws Exception {
        nettyctx.flush();
    }
	
    
}
