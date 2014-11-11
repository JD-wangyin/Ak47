package com.wangyin.ak47.core.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.wangyin.ak47.core.message.SimpleRequest;
import com.wangyin.ak47.core.message.SimpleResponse;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.HandlerContext;
import com.wangyin.ak47.core.Message;
import com.wangyin.ak47.core.Service;


/**
 * 用于实现service，
 * 在HandlerChain中，必须在CodecDriverHandler的后面。
 * 
 * @author wyhanyu
 *
 * @param <D>
 * @param <U>
 */
public class ServiceDriverHandler<O, I> extends HandlerAdapter<O, I> { 
    private static final Logger log = new Logger(ServiceDriverHandler.class);
    
    private Map<String, Service<O, I>> serviceChain;
    
    public ServiceDriverHandler(){
        serviceChain = new HashMap<String, Service<O, I>>();
    }
    
    public void addService(String name, Service<O, I> service) {
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
            SimpleResponse<I> response = new SimpleResponse<I>(msg);
            Message<O> reqmsg = msg.newMessage();
            SimpleRequest<O> request = new SimpleRequest<O>(reqmsg);
            
            Iterator<Entry<String, Service<O, I>>> it = serviceChain.entrySet().iterator();
            while(it.hasNext()){
                Entry<String, Service<O, I>> en = it.next();
                String name = en.getKey();
                Service<O, I> service = en.getValue();
                log.debug("doService {}", name);
                service.doService(request, response);
                if( response.isDone() ){
                    break;
                }
            }
        }
        
        ctx.fireReceived(msg);
    }


}
