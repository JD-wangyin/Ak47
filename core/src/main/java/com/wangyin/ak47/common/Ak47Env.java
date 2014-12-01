package com.wangyin.ak47.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;



/**
 * AK47 environment variables.
 * 
 * AK47的环境变量
 * 
 * @author hannyu
 *
 */
public class Ak47Env {
    

    
    // if running from jar
    public static final boolean IF_RUNNING_FROM_JAR = Ak47Env.class.getResource(
            Ak47Env.class.getSimpleName()+".class").getProtocol().equals("jar");// $NON-NLS-1$ $NON-NLS-2$
            

    // AK47 pageck root
    public static final String AK47_PACKAGE_ROOT = Ak47Env.class.getPackage().getName();
    
    // mockserver find package path
    //public static final String MOCKSERVER_FIND_PACKAGE = MockServer.class.getPackage().getName();
    public static final String MOCKSERVER_FIND_PACKAGE = AK47_PACKAGE_ROOT + ".mocklets";
    
    // all contains the last "/"
    // ak47 local share
    public static final String AK47_LOCAL_SHARE_DIR;
    static {
        String tmpDir = System.getProperty("ak47.localShare","");
        if (tmpDir.length() == 0) {
            tmpDir = System.getProperty("user.home") + File.separator 
                    + ".ak47"+ File.separator;
        }
        AK47_LOCAL_SHARE_DIR = tmpDir;
        System.setProperty("ak47.localShare", AK47_LOCAL_SHARE_DIR);
    }
    
    // ak47 home
    public static final String AK47_HOME_DIR;
    public static final String AK47_HOME_CONF_DIR;
    public static final String AK47_HOME_BIN_DIR;
    public static final String AK47_HOME_LIB_DIR;
    public static final String AK47_HOME_DATA_DIR;
    public static final String AK47_HOME_LOG_DIR;
    
    // ext_dir is NOT fixed
    public static String AK47_HOME_EXT_DIR;
    static {
        
        String jarUrl = Ak47Env.class.getProtectionDomain().getCodeSource().getLocation()
                .getFile();
        String jarPath = "./";
        try{
            jarPath = URLDecoder.decode(jarUrl, "UTF-8");
        }catch(UnsupportedEncodingException e){
            System.err.println("APP_CONF_DIR init fail. jarUrl is "+jarUrl+".");
        }
        
        File jarFile = new File(jarPath);
        AK47_HOME_DIR = jarFile.getAbsoluteFile().getParentFile().getParent() + File.separator;
        AK47_HOME_CONF_DIR = AK47_HOME_DIR + "conf";
        AK47_HOME_BIN_DIR = AK47_HOME_DIR + "bin";
        AK47_HOME_LIB_DIR = AK47_HOME_DIR + "lib";
        AK47_HOME_DATA_DIR = AK47_HOME_DIR + "data";
        AK47_HOME_LOG_DIR = AK47_HOME_DIR + "log";
        AK47_HOME_EXT_DIR = AK47_HOME_DIR + "ext";
        System.setProperty("ak47.home", AK47_HOME_DIR);
        
    } 
    
    
    
    
}
