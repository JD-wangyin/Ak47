package com.wangyin.ak47.core.handler;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.wangyin.ak47.core.message.SimpleRequest;
import com.wangyin.ak47.core.message.SimpleResponse;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.FutureListener;
import com.wangyin.ak47.core.Future;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Service;


/**
 * 用于实现service，
 * 在HandlerChain中，必须在CodecStubHandler的后面。
 * 
 * @author hannyu
 *
 * @param <O>
 * @param <I>
 */
public class ServiceStubHandler<O, I> extends HandlerAdapter<O, I> {
    private static final Logger log = new Logger(ServiceStubHandler.class);
    
    private Map<String, Service<I, O>> serviceChain;
    
    
    public ServiceStubHandler(){
        serviceChain = new LinkedHashMap<String, Service<I, O>>();
    }
    

    public void addService(String name, Service<I, O> service) {
        serviceChain.put(name, service);
    }
    
    public void removeService(String name){
        serviceChain.remove(name);
    }

    @Override
    public void doReceived(HandlerContext<O, I> ctx, Message<I> msg) throws Exception {
        
        if( serviceChain.size() == 0 ){
            log.warn("Ignored because there is NO service.");
        }else{
            SimpleRequest<I> request = new SimpleRequest<I>(msg);
            Message<O> resmsg = msg.newMessage();
            SimpleResponse<O> response = new SimpleResponse<O>(resmsg);
            
            Iterator<Entry<String, Service<I, O>>> it = serviceChain.entrySet().iterator();
            while(it.hasNext()){
                Entry<String, Service<I, O>> en = it.next();
                String name = en.getKey();
                Service<I, O> service = en.getValue();
                log.debug("doService {}", name);
                service.doService(request, response);
                if( response.isDone() ){
                    break;
                }
            }
            
            if(response.hasPojo()){
                Future<O, I> future = ctx.send(resmsg);
                if(response.isDisconnectOnComplete()){
                    future.addListener(FutureListener.CLOSE_ON_COMPLETE);
                }
            }else{
                log.debug("Do all service done, but no response.");
                if(response.isDisconnectOnComplete()){
                    ctx.disconnect();
                }
            }
            
        }
        
        // why do this?
        ctx.fireReceived(msg);
    }

}
