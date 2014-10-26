package com.wangyin.ak47.core.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import com.wangyin.ak47.common.Ak47Constants;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerChain;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.driver.SimpleDriver;
import com.wangyin.ak47.core.handler.BlockingQueueDriverHandler;
import com.wangyin.ak47.core.handler.CodecDriverHandler;
import com.wangyin.ak47.core.handler.LoggingOutbandHandler;
import com.wangyin.ak47.core.handler.HandlerInitializer;
import com.wangyin.ak47.core.handler.ServiceDriverHandler;

public class NettySimpleDriver<Q, R> implements SimpleDriver<Q, R>{
    private static final Logger log = new Logger(NettySimpleDriver.class);
    
    protected String addr;
    protected int port;
    @Override
    public void setAddr(String addr) {
        this.addr = addr;
    }
    @Override
    public void setPort(int port) {
        this.port = port;
    }
    
    protected Pipe<Q, R> pipe;
    protected HandlerInitializer<Q, R> stubInitializer;
    protected HandlerInitializer<Q, R> userInitializer;
    @Override
    public void userInitializer(HandlerInitializer<Q, R> userInitializer) {
        this.userInitializer = userInitializer;
    }
    
    private ServiceDriverHandler<Q, R> serviceDriverHandler;
    public void addService(String name, Service<Q, R> service){
        serviceDriverHandler.addService(name, service);
    } 
    public void delService(String name){
        serviceDriverHandler.removeService(name);
    }
    
    protected Bootstrap bootstrap;
    protected Channel channel;
    protected BlockingQueueDriverHandler<Q, R> blockingQueueDriverHandler;
    
    public NettySimpleDriver(Pipe<Q, R> pipe){
        this.pipe = pipe;
        initDriverHandler();
    }
    
    protected void initDriverHandler(){

        final LoggingOutbandHandler<Q, R> logOutboundHandler = new LoggingOutbandHandler<Q, R>();
        final CodecDriverHandler<Q, R> codecDriverHandler = new CodecDriverHandler<Q, R>(pipe);
        serviceDriverHandler = new ServiceDriverHandler<Q, R>(pipe);
        blockingQueueDriverHandler = new BlockingQueueDriverHandler<Q, R>();
        
        stubInitializer = new HandlerInitializer<Q, R>(){
            @Override
            public void initHandler(HandlerChain<Q, R> chain) {
                chain.addLast("LoggingOutboundHandler", logOutboundHandler);
                chain.addLast("CodecDriverHandler", codecDriverHandler);
                chain.addLast("ServiceDriverHandler", serviceDriverHandler);
                chain.addLast("BlockingQueueDriverHandler", blockingQueueDriverHandler);
            }
        };
    }
    
    private void initBootstrap(){
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        
        bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_RCVBUF, Ak47Constants.SO_RCVBUF)
                .option(ChannelOption.SO_SNDBUF, Ak47Constants.SO_SNDBUF)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_BACKLOG, Ak47Constants.SO_BACKLOG);
                
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch)
                    throws Exception {
                
                NettyChannel<Q, R> channel = new NettyChannel<Q, R>(ch);
                channel.chain()
                        .addLast("StubInitializer", stubInitializer)
                        .addLast("UserInitializer", userInitializer);

                NettyChannelHandlerAdapter<Q, R> nettyChannelHandler = 
                        new NettyChannelHandlerAdapter<Q, R>(channel, pipe, pipe.newExecutor());
                
                ch.pipeline().addLast("NettyChannelHandlerAdapter", nettyChannelHandler);
            }
        });
        
        log.debug("Driver init success.");
    } 

    @Override
    public R send(final Q q) throws Exception {
        if( null == bootstrap ){
            initBootstrap();
        }
        
        if( null == q ){
            throw new IllegalArgumentException("Q is null.");
        }
        
        if( null != channel && !channel.isActive() ){
            channel.close();
            channel = null;
        }
        
        if( channel == null ){
            channel = bootstrap.connect(new InetSocketAddress(addr, port))
                    .awaitUninterruptibly().channel();
//            bootstrap
//                .connect(new InetSocketAddress(addr, port))
//                .addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        if( future.isSuccess() ){
//                            channel = future.channel();
//                            blockingQueueDriverHandler.send(q);
//                        }else{
//                            throw new Ak47RuntimeException("Fail to connect to "+addr+":"+port);
//                        }
//                    }
//                    
//                });
        }
        blockingQueueDriverHandler.send(q);
        
        return blockingQueueDriverHandler.recv();
    }

    @Override
    public R sendAndClose(Q q) throws Exception {
        R r = send(q);
        close();
        return r;
    }

    @Override
    public void close() throws Exception{
        if( channel != null && channel.isActive() ){
            channel.close().sync();
        }
    }
    
    /**
     * 释放所有资源，不可再使用
     * 
     * @throws Exception
     */
    public void release(){
        try {
            close();
        } catch (Exception e) {
        }
        bootstrap.group().shutdownGracefully();
        channel = null;
        bootstrap = null;
        
    }


}
