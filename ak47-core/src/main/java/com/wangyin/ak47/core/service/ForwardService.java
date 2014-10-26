package com.wangyin.ak47.core.service;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.driver.SimpleDriver;

/**
 * 转发service
 * 注意：这个service是阻塞的，即发送之后会一直等待直到收到返回。
 * 不能用作压力桩，若要使用高性能的代理，请参考ProxyServer 。
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public class ForwardService<Q, R> implements Service<Q, R> {
    private static final Logger log = new Logger(ForwardService.class);
    
    private Pipe<Q, R> pipe;
    private String forwardAddr;
    private int forwardPort;
    private boolean isLongConnection;
    
    /**
     * 初始化必须要带上转发到的目的地址和端口
     * 
     * @param forwardAddr
     * @param forwardPort
     */
    public ForwardService(Pipe<Q, R> pipe, String forwardAddr, int forwardPort){
        this(pipe, forwardAddr, forwardPort, false);
    }
    
    public ForwardService(Pipe<Q, R> pipe, String forwardAddr, int forwardPort, boolean isLongConnection){
        this.pipe = pipe;
        this.forwardAddr = forwardAddr;
        this.forwardPort = forwardPort;
        this.isLongConnection = isLongConnection;
    }

    
    
    private SimpleDriver<Q, R> fwdDriver;
    /**
     * 1、每次收到数据将新建connection。
     * 2、每次doservice结束将close connection。
     * 3、只能用于短连接，不能用于长连接。
     * 4、本service性能会比较差，只能用于功能测试、抓包分析、录制数据上，不能用于压力桩。
     * 5、ForwardService会多进行一次 encodeRequest 和 decodeResponse
     */
    @Override
    public void doService(Request<Q> request, Response<R> response)
            throws Exception {
        if( isLongConnection && null != fwdDriver){
            // nothing
        }else{
            fwdDriver = pipe.createSimpleDriver(forwardAddr, forwardPort);
        }
        
        R r = fwdDriver.send(request.pojo());
        response.pojo(r);
        
        if( !isLongConnection ){
            fwdDriver.release();
            fwdDriver = null;
        }
        log.info("forward success.");
    }

}
