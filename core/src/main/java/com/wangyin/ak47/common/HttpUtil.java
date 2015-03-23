package com.wangyin.ak47.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



/**
 * HTTP-related helper class
 * 
 * HTTP协议相关的辅助方法
 * 
 * @author hannyu
 *
 */
public class HttpUtil {
    
    
    /**
     * Convert {@code Map<String, String>} to body of HTTP, such as:
     * 
     *  Map {
     *      wd: ""
     *      zxmode: "1"
     *      json: "1"
     *      p: "3"
     *      bs: "test ak47"
     *  }                     {@code ===>      wd=&zxmode=1&json=1&p=3&bs=test%20ak47}
     * 
     * 
     * 将Map转化为Http协议的body
     * 
     * @param bodyMap map of http body
     * @return string of content
     * @throws UnsupportedEncodingException 
     */
    public static String map2Body(Map<String, String> bodyMap) 
            throws UnsupportedEncodingException{
        
        StringBuilder sb = new StringBuilder(64);
        int i = 0;
        for(Entry<String, String> en : bodyMap.entrySet() ){
            if( i>0 ){
                sb.append("&");
            }
            i++;
            
            String name = en.getKey();
            String value = en.getValue();
            sb.append(name).append("=").append( 
                    URLEncoder.encode(value, Ak47Constants.DEFAULT_ENCODING));
            
        }
        return sb.toString();
    }
    
    /**
     * Convert body of HTTP to {@code Map<String, String>}, such as:
     * 
     *  {@code wd=&zxmode=1&json=1&p=3&bs=test%20ak47   ===>}   
     *                            Map {
     *                                wd: ""
     *                                zxmode: "1"
     *                                json: "1"
     *                                p: "3"
     *                                bs: "test ak47"
     *                            }
     * 
     * 将Http的body转化为Map
     * 
     * @param body string of http body
     * @return map of http body
     * @throws UnsupportedEncodingException 
     */
    public static Map<String, String> body2Map(String body) throws UnsupportedEncodingException{
        Map<String, String> bodyMap = new HashMap<String, String>();
        String[] nvs = body.split("&");
        for(String nv : nvs){
            if( nv.length() > 0 ){
                String[] ns = nv.split("=",2);
                if( ns.length == 2 ){
                    String name = ns[0];
                    String value = URLDecoder.decode(ns[1], Ak47Constants.DEFAULT_ENCODING);
                    if( name.length() > 0 ){
                        bodyMap.put(name, value);
                    }
                }
            }
        }
        return bodyMap;
    }

}
