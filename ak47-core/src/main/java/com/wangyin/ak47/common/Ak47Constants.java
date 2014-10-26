package com.wangyin.ak47.common;


/**
 * AK47 constants variables.
 * 
 * AK47的常量
 * 
 * @author hannyu
 *
 */
public class Ak47Constants {
    
    //////////////////////// base
    
    // name
    public static final String NAME = "AK47";
    
    // version
    public static final String VERSION = "1.0.0";
    
    // default encoding
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    
    ////////////////////// Network
    
    // socket option determining the number of connections queued
    public static final int SO_BACKLOG = 10000;
    
    /**
     * The SO_RCVBUF option is used by the platform's networking code as 
     * a hint for the size to set the underlying network I/O buffers.
     * 
     * Increasing the receive buffer size can increase the performance 
     * of network I/O for high-volume connection, while decreasing it can 
     * help reduce the backlog of incoming data.
     * 
     */
    public static final int SO_RCVBUF = 1024 * 8;
    
    /**
     * The SO_SNDBUF option is used by the platform's networking code as 
     * a hint for the size to set the underlying network I/O buffers.
     */
    public static final int SO_SNDBUF = 1024 * 8;
}
