package com.wangyin.ak47.core;

import com.wangyin.ak47.core.util.RequestAttr;
import com.wangyin.ak47.core.util.SessionAttr;


public interface Response<R> {
    
    
    public boolean hasPojo();
    public R pojo();
    public void pojo(R pojo);

    public RequestAttr requestAttr();
    public SessionAttr sessionAttr();
    
    public void disconnectOnComplete();
    public void done();
    
}
