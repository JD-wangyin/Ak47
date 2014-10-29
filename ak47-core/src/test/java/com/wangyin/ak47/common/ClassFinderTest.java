package com.wangyin.ak47.common;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;

import org.junit.Assert;
import org.junit.Test;

import com.wangyin.ak47.common.ClassFinder;
import com.wangyin.ak47.common.ClassFinder.ClassFinderFilter;

public class ClassFinderTest {
    
    @Test
    public void testme() throws ZipException, IOException{
        List<Class<?>> list1 = ClassFinder.findClassesInJars("", 
            new ClassFinderFilter(){
                @Override
                public boolean accept(Class<?> clazz) {
                    return clazz.getName().endsWith("Handler");
                }
        });
        Assert.assertTrue(list1.isEmpty());
        
    }

    public static void main(String[] args) throws ZipException, IOException {
        
        
        List<Class<?>> list1 = ClassFinder.findClassesInJars("D:\\maven.m2\\repository\\io\\netty\\netty-all", 
                new ClassFinderFilter(){

                    @Override
                    public boolean accept(Class<?> clazz) {
                        return clazz.getName().endsWith("Handler");
                    }
            
        });
        
        
        System.out.println("=========================================");
        for( Class<?> clazz : list1 ){
            System.out.println("class: " + clazz.getName());
        }
    }
}
