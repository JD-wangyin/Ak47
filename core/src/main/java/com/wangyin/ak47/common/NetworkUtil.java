package com.wangyin.ak47.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Network-related helper class
 * 
 * 网络辅助类
 * 
 * @author hannyu
 *
 */
public class NetworkUtil {
    
    /**
     * Get local IP
     * 
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalIp() throws UnknownHostException{
        String ip = InetAddress.getLocalHost().getHostAddress();
        return ip;
    }
}
