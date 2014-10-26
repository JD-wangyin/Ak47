package com.wangyin.ak47.core.record;

import java.util.ArrayList;
import java.util.List;

import com.wangyin.ak47.common.TimeUtil;


/**
 * 专用于记录输入输出的最小单位Record，
 * 每个Record均包含n个Request和m个Response，m、n均大于等于0.
 * 
 * @author wyhanyu
 *
 * @param <Q> request
 * @param <R> response
 */
public class Record<Q, R> {

    private String ts;

    private List<String> tags;
    private Q q;
    private R r;
    


    public Record(Q q, R r){
        ts = TimeUtil.currentTimeString();
        tags = new ArrayList<String>();
        this.q = q;
        this.r = r;
    }
    
    public Record(){
        this(null,null);
    }
    
    public Q getQ(){
        return q;
    }
    public void setQ(Q q){
        this.q = q;
    }
    
    public R getR(){
        return r;
    }
    public void setR(R r){
        this.r = r;
    }
    
    public List<String> getTags(){
        return tags;
    }
    public void setTags(List<String> tags){
        this.tags = tags;
    }
    
    public String getTs() {
        return ts;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Record[@").append(ts).append(",q:").append(q==null?"null":q.getClass().getName())
                .append(",r:").append(r==null?"null":r.getClass().getName()).append(",tag:");
        for(String tag : tags){
            sb.append(tag).append("/");
        }
        sb.append("]");
        return sb.toString();
    }
    
    
}
