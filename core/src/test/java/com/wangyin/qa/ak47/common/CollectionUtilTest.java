package com.wangyin.qa.ak47.common;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.wangyin.ak47.common.CollectionUtil;

public class CollectionUtilTest {
    
    @Test
    public void testme(){
        String[] s1 = new String[]{"aaa", "bbb", "ccc", "ddd"};
        String[] s2 = CollectionUtil.shift(s1);
        
        Assert.assertEquals(3, s2.length);
        Assert.assertEquals("bbb", s2[0]);
        Assert.assertEquals("ddd", s2[2]);
    }
    
}
