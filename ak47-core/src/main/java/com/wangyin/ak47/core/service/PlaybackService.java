package com.wangyin.ak47.core.service;

import java.io.IOException;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Service;
import com.wangyin.ak47.core.record.Record;
import com.wangyin.ak47.core.record.RecordPool;


/**
 * 负责回放
 * 
 * @author wyhanyu
 *
 * @param <Q>
 * @param <R>
 */
public class PlaybackService<Q, R> implements Service<Q, R> {
    private static final Logger log = new Logger(PlaybackService.class);
    

    private int index;
    private int size;
    private RecordPool<Q, R> recordPool;
    
    public PlaybackService(String ymlPath) throws IOException{
        recordPool = new RecordPool<Q, R>(ymlPath);
        log.debug("need load records on {}.", ymlPath);
        recordPool.load();
        size = recordPool.size();
        log.debug("load {} records.", size);
        
    }


    @Override
    public void doService(Request<Q> request, Response<R> response)
            throws Exception {
        
        Record<Q, R> record = recordPool.get(index%size);
        request.pojo(record.getQ());
        response.pojo(record.getR());
        
        log.info("palyback NO.{} record.", index);
        index++;
    }

}
