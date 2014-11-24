package com.wangyin.qa.ak47.common;

import junit.framework.Assert;

import org.junit.Test;

import com.wangyin.ak47.common.TimeUtil;

public class TimeUtilTest {

    @Test
    public void testme(){
        
        String s1 = TimeUtil.currentTimeString();
        
        Assert.assertEquals("yyyy-MM-dd HH:mm:ss".length(), s1.length());
        
    }
}
