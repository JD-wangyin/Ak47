package com.wangyin.qa.ak47.common;

import junit.framework.Assert;

import org.junit.Test;

import com.wangyin.ak47.common.StringUtil2;

public class StringUtil2Test {
    
    @Test
    public void testme(){
        String s1 = "Content-Type";
        
        String s2 = StringUtil2.asciiToLowerCase(s1);
        Assert.assertEquals("content-type", s2);
        
        String s3 = StringUtil2.asciiToUpperCase(s1);
        Assert.assertEquals("CONTENT-TYPE", s3);
        
    }
    
    public static void main(String[] args) {
        
        String s1 = "content-Type";
        
        System.out.println(StringUtil2.asciiToLowerCase(s1));
        
        long b1 = System.nanoTime();
        for(int i=0;i<10000;i++){
            s1.toLowerCase();
        }
        long e1 = System.nanoTime();
        System.out.println("s1.toLowerCase(): "+(e1-b1)/1000000);
        
        long b2 = System.nanoTime();
        for(int i=0;i<10000;i++){
            StringUtil2.asciiToLowerCase(s1);
        }
        long e2 = System.nanoTime();
        System.out.println("StringUtil2.toLowerCase: "+(e2-b2)/1000000);
        
    }

}
