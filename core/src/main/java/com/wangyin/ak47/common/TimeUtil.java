package com.wangyin.ak47.common;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Time-related helper class.
 * 
 * 时间相关的操作放这里
 * 
 * @author hannyu
 *
 */
public class TimeUtil {

    public static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat standardFormat;
    
    /**
     * Returns the current time in seconds.
     * 
     * @return
     */
    public static final long currentTimeSecond(){
        return System.currentTimeMillis()/1000;
    }
    
    /**
     * Formats NOW into a pattern "yyyy-MM-dd HH:mm:ss"
     * 
     * @return
     */
    public static String currentTimeString(){
        if( standardFormat == null ){
            standardFormat = new SimpleDateFormat(STANDARD_PATTERN);
        }
        return standardFormat.format(new Date());
    }
    
    /**
     * Formats NOW into a specific pattern.
     * 
     * @param pattern
     * @return
     */
    public static String currentTimeString(String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }
    
}
