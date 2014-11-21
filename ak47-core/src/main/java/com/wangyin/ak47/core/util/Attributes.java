package com.wangyin.ak47.core.util;

import java.util.HashMap;
import java.util.Map;


public class Attributes { 
    
    private Map<String, Object> attrmap;
    
    public Attributes(){
        attrmap = new HashMap<String, Object>();
    }
    
    public void set(String key, Object value){
        attrmap.put(key, value);
    }
    
    public Object get(String key){
        return attrmap.get(key);
    }
    
    public boolean has(String key){
        return attrmap.containsKey(key);
    }
    
    public void remove(String key){
        attrmap.remove(key);
    }

}
