package com.wangyin.ak47.core;

import com.wangyin.ak47.core.util.RequestAttr;
import com.wangyin.ak47.core.util.SessionAttr;


public interface Request<Q> {
    
    public long order();
    
    public boolean hasPojo();
    public Q pojo();
    public void pojo(Q pojo);
    
    public RequestAttr requestAttr();
    public SessionAttr sessionAttr();
    
}



