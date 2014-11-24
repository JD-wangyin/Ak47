package com.wangyin.ak47.common;

import io.netty.util.internal.ThreadLocalRandom;

/**
 * 随机数辅助类
 * 
 * @author wyhubingyin
 * 
 */
public class RandomUtil {
    
    
    /**
     * = ThreadLocalRandom.current().nextInt(max)
     * 
     * @param max
     * @return
     */
    public static final int nextInt(int max){
        return ThreadLocalRandom.current().nextInt(max);
    }
    
    
}
