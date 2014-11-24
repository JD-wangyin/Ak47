package com.wangyin.ak47.common;

import java.io.IOException;
import java.util.Map;

import com.wangyin.ak47.core.exception.Ak47RuntimeException;


/**
 * AK47 default global configuration. 
 * Prefix {@see ConfigLoader} is 'ak47'.
 * 
 * AK47默认的自身的全局配置。
 * 
 * @author hannyu
 *
 */
public class Ak47Config {
    
    private static final String PREFIX = "ak47";
    
    private static Map<String, Object> configMap;
    
    
    /**
     * Make sure the config file has been loaded.
     * 
     * 确保配置已加载过。
     */
    public static void load(){
        if( null == configMap ){
            reload();
        }
    }
    
    
    /**
     * Load it, regardless of whether has loaded before.
     * 
     * 加载配置，不管之前有无加载过。
     */
    public static void reload(){
        try {
            configMap = ConfigLoader.load(PREFIX);
        } catch (IOException e) {
            throw new Ak47RuntimeException("Ak47Config load fail.", e);
        }
        
    }

    
    /**
     * If contains key
     * 
     * 是否包含某配置？
     * 
     * @param key
     * @return
     */
    public static boolean containsKey(String key){
        load();
        return configMap.containsKey(key);
    }
    
    
    /**
     * Return the value, null if NOT contains key.
     * 
     * 返回对象，自行解读
     * 
     * @param key
     * @return
     */
    public static Object get(String key){
        load();
        return configMap.get(key);
    }
    
    
    /**
     * Return the value in String, "" if NOT contains key.
     * 
     * 返回string
     * 
     * 注： 如果 key 不存在，那么会返回 ""，注意不是 null。
     * 
     * @param key
     * @return
     */
    public static String getString(String key){
        Object obj = get(key);
        if( null == obj ){
            return "";
        }else{
            return (String) obj;
        }
    }
    
    
    /**
     * Return the value in Integer, 0 if NOT contains key.
     * 
     * 返回 int
     * 
     * 注： 如果 key 不存在，那么会返回 0。
     * @param key
     * @return
     */
    public static int getInt(String key){
        Object obj = get(key);
        if( null == obj ){
            return 0;
        }else{
            return (Integer) obj;
        }
    }
    
    /**
     * Return true or false, false if NOT contains key.
     * 
     * 返回true或false，如果不存在则返回false。
     * 
     * @param key
     * @return
     */
    public static boolean getBoolean(String key){
        Object obj = get(key);
        if( null == obj ){
            return false;
        }else{
            return (Boolean) obj;
        }
    }
}
