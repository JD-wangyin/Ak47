package com.wangyin.ak47.core.netty;


import java.net.InetSocketAddress;

import com.wangyin.ak47.core.stress.BenchResult;
import com.wangyin.ak47.core.stress.Clip;
import com.wangyin.ak47.core.stress.Collector;
import com.wangyin.ak47.core.HandlerChain;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.driver.StressDriver;
import com.wangyin.ak47.core.handler.HandlerInitializer;
import com.wangyin.ak47.core.handler.StressDriverHandler;
import com.wangyin.ak47.core.handler.CodecDriverHandler;

public class NettyStressDriver<Q, R> extends NettySimpleDriver<Q, R> implements StressDriver<Q, R> {
    
    private StressDriverHandler<Q, R> stressDriverHandler;
    
    
    public NettyStressDriver(Pipe<Q, R> pipe) {
        super(pipe);
    }
    
    
    protected void initDriverHandler(){
        stressDriverHandler = new StressDriverHandler<Q, R>(this, pipe);
        final CodecDriverHandler<Q, R> codecDriverHandler = new CodecDriverHandler<Q, R>(pipe);
        
        stubInitializer = new HandlerInitializer<Q, R>(){
            @Override
            public void initHandler(HandlerChain<Q, R> chain) {
                chain.addLast("CodecDriverHandler", codecDriverHandler);
                chain.addLast("StressDriverHandler", stressDriverHandler);
            }
        };
    }
    
    private InetSocketAddress sockaddr;
    @Override
    public void newConnection() {
        if( sockaddr == null ){
            sockaddr = new InetSocketAddress(addr, port);
        }
        bootstrap.connect(sockaddr).channel();
    }

    @Override
    public void loadClip(Clip<Q> clip) {
        if( null == clip ) {
            throw new IllegalArgumentException("Clip should NOT be null.");
        }
        stressDriverHandler.loadClip(clip);
    }
    
    @Override
    public BenchResult bench() {
        stressDriverHandler.bench();
        return null;
    }

    @Override
    public BenchResult bench(int concurrency) {
        stressDriverHandler.bench(concurrency);
        return null;
    }
    
    @Override
    public BenchResult bench(int concurrency, boolean keepalive) {
        stressDriverHandler.bench(concurrency, keepalive);
        return null;
    }

    @Override
    public BenchResult bench(int concurrency, boolean keepalive, int tpsLimit) {
        stressDriverHandler.bench(concurrency, keepalive, tpsLimit);
        return null;
    }

    @Override
    public boolean isOver() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void hold() {
        // TODO Auto-generated method stub
    }

    @Override
    public void holdUntilFiredNum(long num) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void holdUntilSeconds(int seconds) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCollector(Collector<Q, R> collector) {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public void release() {
        // TODO Auto-generated method stub
        
    }
    
}
