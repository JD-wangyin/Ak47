package com.wangyin.ak47.pipes.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wangyin.ak47.common.ByteUtil;


/**
 * 简单的HttpRequest实现
 * 
 * @author wyhanyu
 *
 */
public class SimpleHttpMessage {
    

    
    // HttpMessage = startLine + headers + content;
    // first line
    private String startLine;
    // many headers
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();;
    // any content
    private byte[] content;

    public String getStartLine(){
        return startLine;
    }
    public void setStartLine(String startLine){
        this.startLine = startLine;
    }
    
    public Map<String, List<String>> getHeaders(){
        return headers;
    }
    public void setHeaders(Map<String, List<String>> headers){
        this.headers = headers;
    }
    
    public byte[] getContent(){
        return content;
    }
    public void setContent(byte[] content){
        this.content = content;
    }
    
    /**
     * 添加一个Header
     * 
     * @param key
     * @param value
     */
    public void addHeader(String key, String value){
//        key = StringUtil2.asciiToLowerCase(key);
        List<String> vl = headers.get(key);
        if( null == vl ){
            vl = new ArrayList<String>(1);
            vl.add(value);
            headers.put(key, vl);
        }else{
            vl.add(value);
        }
    }
    
    /**
     * 获取该key的第一个header
     * 若不存在，则返回null
     * 
     * @param key
     * @return 
     */
    public String getHeaderFirst(String key){
//        key = StringUtil2.asciiToLowerCase(key);
        List<String> vl = headers.get(key);
        if( null == vl ){
            return null;
        }else {
            return vl.get(0);
        }
    }
    
    /**
     * 修改key的header
     * @param key
     * @param value
     */
    public void setOrAddFirstHeader(String key, String value){
//        key = StringUtil2.asciiToLowerCase(key);
        List<String> vl = headers.get(key);
        if( null == vl ){
            addHeader(key, value);
        }else {
            vl.set(0, value);
        }
    }
    
    /**
     * build HTTP header
     * 
     * @return
     */
    public String buildHeaderString(){
        StringBuilder sb = new StringBuilder();
        sb.append(startLine).append(SimpleHttpParser.CRLF_STRING);
        for(String key : headers.keySet()){
            List<String> vl = headers.get(key);
            for(String v : vl){
                if( key.equals("content-length") ){
                    key = "Content-Length";
                }
                if( key.equals("server") ){
                    key = "Server";
                }
                sb.append(key).append(": ").append(v).append(SimpleHttpParser.CRLF_STRING);
                
            }
        }
        sb.append(SimpleHttpParser.CRLF_STRING);
        
        return sb.toString();
    }
    
    /**
     * 完整打印
     * 
     * @return
     */
    @Override
    public String toString(){
        if( null != content ){
            StringBuilder sb = new StringBuilder();
            sb.append( buildHeaderString() );
            sb.append( "content("+content.length+")" );
            return sb.toString();
        }else{
            return buildHeaderString();
        }
    }
    
    
    /**
     * 导出为bytearray
     * @return
     */
    public byte[] buildFullBytes(){
        byte[] half = buildHeaderString().getBytes();
        if( null != content ){
            byte[] full = ByteUtil.merge(half,content);
            return full;
        }else{
            return half;
        }
    }
}
