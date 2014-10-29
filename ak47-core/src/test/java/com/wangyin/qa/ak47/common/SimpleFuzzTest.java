package com.wangyin.qa.ak47.common;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.wangyin.ak47.common.SimpleFuzz;

public class SimpleFuzzTest {

    @Test
    public void testme(){
        SimpleFuzz fuzz = new SimpleFuzz(123);
        
        String s1 = "1111111111_1111111111_1111111111_1111111111_1111111111_" +
                "1111111111_1111111111_1111111111_1111111111_1111111111_";
        byte[] b1 = s1.getBytes();
        byte[] b2 = fuzz.fuzz(b1, 20);
        
        Assert.assertEquals(b1, s1.getBytes());
        Assert.assertNotEquals(b1, b2);
        
    }
    
    public static void main(String[] args) {
        SimpleFuzz fuzz = new SimpleFuzz(123);
        
        String s1 = "1111111111_1111111111_1111111111_1111111111_1111111111_" +
                "1111111111_1111111111_1111111111_1111111111_1111111111_";
        byte[] b1 = s1.getBytes();
        
        System.out.println( new String(fuzz.fuzz(b1, 10)) );
        System.out.println( new String(fuzz.fuzz(b1, 50)) );
        System.out.println( new String(fuzz.fuzz(b1, 100)) );
        System.out.println( new String(fuzz.fuzz(b1, 500)) );
        System.out.println( new String(fuzz.fuzz(b1, 1000)) );
    }
}
