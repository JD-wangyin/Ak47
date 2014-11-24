package com.wangyin.ak47.common;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.wangyin.ak47.common.Ak47Env;


public class AK47EnvTest {
    
    @Test
    public void testme(){
        Assert.assertTrue( Ak47Env.AK47_PACKAGE_ROOT.length()>0 );
    }
    
    
    public static void main(String[] args) {
        System.out.println("AK47_PACKAGE_ROOT: "+Ak47Env.AK47_PACKAGE_ROOT);
        System.out.println("AK47_LOCAL_SHARE_DIR: "+Ak47Env.AK47_LOCAL_SHARE_DIR);
        System.out.println("AK47_HOME_DIR: "+Ak47Env.AK47_HOME_DIR);
        System.out.println("AK47_HOME_BIN_DIR: "+Ak47Env.AK47_HOME_BIN_DIR);
        System.out.println("AK47_HOME_CONF_DIR: "+Ak47Env.AK47_HOME_CONF_DIR);
        System.out.println("AK47_HOME_DATA_DIR: "+Ak47Env.AK47_HOME_DATA_DIR);
        System.out.println("AK47_HOME_LIB_DIR: "+Ak47Env.AK47_HOME_LIB_DIR);
        System.out.println("AK47_HOME_EXT_DIR: "+Ak47Env.AK47_HOME_EXT_DIR);
        System.out.println("AK47_HOME_LOG_DIR: "+Ak47Env.AK47_HOME_LOG_DIR);
        System.out.println("IF_RUNNING_FROM_JAR: "+ (Ak47Env.IF_RUNNING_FROM_JAR?"true":"false"));
        System.out.println(Ak47Env.class.getResource("/"));
    }

}
