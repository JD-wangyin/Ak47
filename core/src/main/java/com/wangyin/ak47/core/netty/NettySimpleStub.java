package com.wangyin.ak47.core.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.wangyin.ak47.core.stub.SimpleStub;
import com.wangyin.ak47.common.Ak47Constants;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerChain;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.handler.CodecStubHandler;
import com.wangyin.ak47.core.handler.FilterStubHandler;
import com.wangyin.ak47.core.handler.LoggingTrafficHandler;
import com.wangyin.ak47.core.handler.HandlerInitializer;
import com.wangyin.ak47.core.handler.ServiceStubHandler;

/**
 * 简单Server
 * 
 * @author wyhanyu
 * 
 * @param <Q>
 * @param <R>
 */
public class NettySimpleStub<Q, R> implements SimpleStub<Q, R> {
    private static final Logger log = new Logger(NettySimpleStub.class);

    private int port;

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    protected Pipe<Q, R> pipe;
    protected HandlerInitializer<R, Q> stubInitializer;
    protected HandlerInitializer<R, Q> userInitializer;
    @Override
    public void userInitializer(HandlerInitializer<R, Q> userInitializer) {
        this.userInitializer = userInitializer;
    }
    
    private ServiceStubHandler<R, Q> serviceStubHandler;
    @Override
    public void addService(String name, Service<Q, R> service) {
        serviceStubHandler.addService(name, service);
    }
    @Override
    public void removeService(String name) {
        serviceStubHandler.removeService(name);
    }

    private ServerBootstrap serverBootstrap;
    private ChannelFuture bindFuture;

    public NettySimpleStub(final Pipe<Q, R> pipe) {
        this.pipe = pipe;
        initStubHandler();
    }
    
    
    protected void initStubHandler(){
        
        final LoggingTrafficHandler<R, Q> logTrafficHandler = new LoggingTrafficHandler<R, Q>();
        final CodecStubHandler<R, Q> codecStubHandler = new CodecStubHandler<R, Q>(pipe);
        final FilterStubHandler<R, Q> filterStubHandler = new FilterStubHandler<R, Q>(pipe);
        serviceStubHandler = new ServiceStubHandler<R, Q>();

        stubInitializer = new HandlerInitializer<R, Q>() {
            @Override
            public void initHandler(HandlerChain<R, Q> chain) {
                chain.addLast("LoggingTrafficHandler", logTrafficHandler);
                chain.addLast("CodecStubHandler", codecStubHandler);
                chain.addLast("FilterStubHandler", filterStubHandler);
                chain.addLast("ServiceStubHandler", serviceStubHandler);
            }
        };
    }


    protected void initServerBootstrap() {
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_RCVBUF, Ak47Constants.SO_RCVBUF)
                .option(ChannelOption.SO_SNDBUF, Ak47Constants.SO_SNDBUF)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_BACKLOG, Ak47Constants.SO_BACKLOG);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                
                NettyChannel<R, Q> channel = new NettyChannel<R, Q>(ch);
                channel.chain().addLast("StubInitializer", stubInitializer);
                if( userInitializer != null ){
                    channel.chain().addLast("UserInitializer", userInitializer);
                }

                NettyChannelHandlerAdapter<R, Q> nettyChannelHandler = 
                        new NettyChannelHandlerAdapter<R, Q>(channel, pipe, pipe.newExecutor());
                
                ch.pipeline().addLast("Ak47ChannelHandlerAdapter", nettyChannelHandler);
            }
        });
        
        log.debug("Stub init success.");
    }

    @Override
    public void start() throws Exception {
        initServerBootstrap();
        bindFuture = serverBootstrap.bind(port);
    }

    @Override
    public void stop() {
        if (bindFuture != null) {
            Channel ch = bindFuture.channel();
            if (ch != null && ch.isActive()) {
                bindFuture.channel().close();
            }
        }
    }

    @Override
    public void hold() throws InterruptedException {
        bindFuture.sync().channel().closeFuture().sync();
    }

    @Override
    public void release() {
        stop();
        serverBootstrap.childGroup().shutdownGracefully();
        serverBootstrap.group().shutdownGracefully();
        serverBootstrap = null;
    }

}
