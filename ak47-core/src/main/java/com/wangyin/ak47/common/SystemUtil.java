package com.wangyin.ak47.common;

/**
 * System-related helper class
 * 
 * 系统相关辅助类
 * 
 * @author hannyu
 *
 */
public class SystemUtil {

    private static final String OS_NAME = System.getProperty("os.name");

    /**
     * Check OS is Linux.
     * 
     * 是否为Linux
     * 
     * @return
     */
    public static boolean isOsLinux(){
        if( OS_NAME.startsWith("Linux") ){
            return true;
        }
        return false;
    }
    
    /**
     * Check OS is Windows.
     * 
     * 是否为Windows
     * 
     * @return
     */
    public static boolean isOsWindows(){
        if( OS_NAME.startsWith("Windows") ){
            return true;
        }
        return false;
    }
    
    
    public static void sleepForever(){
        while(true){
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                //nothing
            }
        }
    }
}
