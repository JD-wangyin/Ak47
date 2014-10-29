package com.wangyin.ak47.common;

import org.junit.Test;

import com.wangyin.ak47.common.PackageUtil;

import junit.framework.Assert;

public class PackageUtilTest {
    
    @Test
    public void testme(){
        String simple = PackageUtil.simplifyName("com.wangyin.ak47.common.PackageUtil");
        Assert.assertEquals("c.w.a.c.PackageUtil", simple);
    }
}
