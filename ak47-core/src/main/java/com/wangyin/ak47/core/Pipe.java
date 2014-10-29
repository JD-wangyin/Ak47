package com.wangyin.ak47.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import com.wangyin.ak47.core.driver.SimpleDriver;
import com.wangyin.ak47.core.driver.StressDriver;
import com.wangyin.ak47.core.event.CurrentThreadEventExecutor;
import com.wangyin.ak47.core.event.ExecutorFactory;
import com.wangyin.ak47.core.netty.NettySimpleDriver;
import com.wangyin.ak47.core.netty.NettySimpleStub;
import com.wangyin.ak47.core.netty.NettyStressDriver;
import com.wangyin.ak47.core.netty.NettyStressStub;
import com.wangyin.ak47.core.stub.SimpleStub;
import com.wangyin.ak47.core.stub.StressStub;
import com.wangyin.ak47.core.Buffer;



/**
 * Pipe is an abstraction, little-like a "two-face man". 
 * 
 * It can be simply understood as a combination of two Channels: 
 *  - one Server-side Channel, which called Stub. 
 *  - one Client-side Channel, which called Driver.
 * 
 * Pipe can become a network entity, only after creating a Stub or Driver.
 *                                             
 *    +--------+     request     +--------+
 *    |        | --------------> |        |       
 *    | Client |                 | Server |      
 *    |        | <-------------- |        |      
 *    +--------+     response    +--------+      
 *                                               
 *                     | |                       
 *                     | |                       
 *                     | |                       
 *                     \ /       +---------------+
 *                      '        |          Pipe |
 *    +--------+     request     +------+        |
 *    |        | --------------> |      |        |
 *    | Client |                 | Stub |        |
 *    |        | <-------------- |      |        |
 *    +--------+     response    |------+        |
 *                               +---------------+
 *                                               
 *                      &                        
 *
 *   +--------------+
 *   | Pipe         |
 *   |       +------+     request     +--------+
 *   |       |      | --------------> |        |
 *   |       |Driver|                 | Server |
 *   |       |      | <-------------- |        |
 *   |       +------|     response    +--------+
 *   +--------------+
 *
 *
 * Pipe是一个抽象概念，有点像是"双面人"，可以简单理解为两个Channel的结合体，
 * 一个Server端的Channel，即Stub，一个Client端的Channel，即Driver，但是同时只能出现一个。
 * 
 * Pipe只有在创建出Stub或Driver之后，才能变成实体。
 * 
 * 
 * @author hannyu
 * 
 * @param <Q>       Request-POJO
 * @param <R>       Response-POJO
 */
public abstract class Pipe<Q, R> implements Codec<Q, R>, Spliter, ExecutorFactory {
    
    /**
     * Create a SimpleStub, for general use, such as Functional-Testing.
     * 
     * 创建一个SimpleStub，可用于一般使用，如功能测试。
     * 
     * @param       port
     * @return
     */
    public SimpleStub<Q, R> createSimpleStub(int port){
        NettySimpleStub<Q, R> ns = new NettySimpleStub<Q, R>(this);
        ns.setPort(port);
        return ns;
    }
    
    /**
     * Create a SimpleDriver, for general use, such as Functional-Testing.
     * 
     * 创建一个SimpleDriver，可用于一般使用，如功能测试。
     * 
     * @param       addr
     * @param       port
     * @return
     */
    public SimpleDriver<Q, R> createSimpleDriver(String addr, int port){
        NettySimpleDriver<Q, R> driver = new NettySimpleDriver<Q, R>(this);
        driver.setAddr(addr);
        driver.setPort(port);
        return driver;
    }
    
    
    /**
     * Create a StressDriver, which can be used for Performance-Testing.
     * 
     * 创建一个StressDriver，可用于性能测试。
     * 
     * @param       addr
     * @param       port
     * @return
     */
    public StressDriver<Q, R> createStressDriver(String addr, int port){
        NettyStressDriver<Q, R> driver = new NettyStressDriver<Q, R>(this);
        driver.setAddr(addr);
        driver.setPort(port);
        return driver;
    }
    
    
    /**
     * Create a StressStub, which can be used for Performance-Testing.
     * 
     * 创建一个StressStub，可用于压力测试。
     * 
     * @param       port
     * @return
     */
    public StressStub<Q, R> createStressStub(int port){
        NettyStressStub<Q, R> stub = new NettyStressStub<Q, R>(this);
        stub.setPort(port);
        return stub;
    }

    /**
     * 适用于pipeline的数据包，避免多线程同时读取。
     * 
     * 注: 只是把划好的包分出来，未划分完整的包不要输出。
     * 另注: 要跟 executor() 结合起来使用。
     * 
     * @param buf
     * @return
     */
    public List<Buffer> split(Buffer buf){
//      return new Buffer[0];
        
//        Buffer[] bufs = new Buffer[1];
//        bufs[0] = buf;
//        return bufs;
        
        List<Buffer> bufs = new ArrayList<Buffer>(1);
        bufs.add(buf);
        return bufs;
    }
    
    
    @Override
    public Executor newExecutor(){
        return new CurrentThreadEventExecutor();
    }
}
