package com.wangyin.qa.ak47.common;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.wangyin.ak47.common.HttpUtil;
import com.wangyin.ak47.common.YmlUtil;

public class HttpUtilTest {
    
    @Test
    public void testme() throws UnsupportedEncodingException{
        String s1 = "area=1&accessToken=b5f869f778521a6d3992323b211a2be9a3&facePrice=30&isp=0&fillType=0";
        Map<String, String> m1 = HttpUtil.body2Map(s1);
        
        Assert.assertEquals(5, m1.size());
        Assert.assertEquals("1", m1.get("area"));
        Assert.assertEquals("b5f869f778521a6d3992323b211a2be9a3", m1.get("accessToken"));
        Assert.assertEquals("30", m1.get("facePrice"));
        Assert.assertEquals("0", m1.get("isp"));
        Assert.assertEquals("0", m1.get("fillType"));
        
        String s2 = HttpUtil.map2Body(m1);
        Assert.assertEquals(s1.length(), s2.length());
        
        Map<String, String> m2 = HttpUtil.body2Map(s2);
        Assert.assertEquals(m1, m2);
        
    }

    
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s1 = "area=1&accessToken=b5f869f778521a6d3992323b211a2be9a3&facePrice=30&isp=0&fillType=0";
        
        Map<String, String> m1 = HttpUtil.body2Map(s1);
        
        System.out.println("res: " + YmlUtil.obj2PrettyYml(m1));
        
        String s2 = HttpUtil.map2Body(m1);
        
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
    }
}
