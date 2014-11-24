package com.wangyin.ak47.core.service;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.record.Record;
import com.wangyin.ak47.core.record.RecordPool;

/**
 * 负责记录输入和输出的数据。
 * 目前只支持YAML文件。
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public class RecordService<Q, R> implements Service<Q, R> {
    private static final Logger log = new Logger(RecordService.class);
    
    private RecordPool<Q, R> recordPool;

    public RecordService(String ymlPath){
        recordPool = new RecordPool<Q, R>(ymlPath);
    }


    @Override
    public void doService(Request<Q> request, Response<R> response) throws Exception {
        Record<Q, R> record = new Record<Q, R>();
        record.setQ(request.pojo());
        record.setR(response.pojo());
        recordPool.add(record);
        
        recordPool.dump();
        log.info("dump success.");
    }

}


