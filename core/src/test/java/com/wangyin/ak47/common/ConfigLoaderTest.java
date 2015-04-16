package com.wangyin.ak47.common;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.wangyin.ak47.common.ConfigLoader;

public class ConfigLoaderTest {
    
    @Test
    public void testme() throws IOException{
        Map<String, Object> m1 = ConfigLoader.load("ak47");
        Assert.assertTrue( m1 != null );
    }

}
