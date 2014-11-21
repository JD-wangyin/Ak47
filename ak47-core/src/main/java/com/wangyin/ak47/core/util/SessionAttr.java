package com.wangyin.ak47.core.util;

public interface SessionAttr {
    
    public void set(String key, Object value);
    
    public Object get(String key);
    
    public boolean has(String key);
    
    public void remove(String key);
}
