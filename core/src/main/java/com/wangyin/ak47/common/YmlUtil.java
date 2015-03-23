package com.wangyin.ak47.common;

import java.io.InputStream;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * YAML-related helper class
 * 
 * Threads must have separate Yaml instances. 
 * Instances are cheap both in terms of time to create and memory to occupy. 
 * Only the very first instance is heavy because of static initializers for constants 
 * and regular expressions.
 * 
 * yaml相关封装
 * 
 * 
 * @author hannyu
 *
 */
public class YmlUtil {
    
    /**
     * Convert Object to YAML-text.
     * 
     * 将Object转化为文本
     * 
     * @param obj
     * @return
     */
    public static String obj2Yml(Object obj){
        Yaml yaml = new Yaml();
        return yaml.dump(obj);
    }
    
    /**
     * Convert Object to human-friendly YAML-text.
     * 
     * 将Object转化为文本，优化打印，不压缩。
     * 
     * @param obj object
     * @return yaml text
     */
    public static String obj2PrettyYml(Object obj){
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        return yaml.dump(obj);
    }
    
    /**
     * Convert YAML-text to Object.
     * 
     * 将文本转化为Object
     * 
     * @param yml yaml text
     * @return object
     */
    public static Object yml2Obj(String yml){
        Yaml yaml = new Yaml();
        return yaml.load(yml);
    }
    
    /**
     * Convert YAML-text to Object.
     * 
     * 将文本转化为Object（适合没有无参构造器的类）
     * 
     * @param yml
     * @param constructor
     * @return
     */
    public static Object yml2Obj(String yml, Constructor constructor){
        Yaml yaml = new Yaml(constructor);
        return yaml.load(yml);
    }

    /**
     * Convert InputStream of YAML-text to Object.
     * 
     * @param is
     * @return
     */
    public static Object stream2Obj(InputStream is){
        Yaml yaml = new Yaml();
        return yaml.load(is);
    }
}
