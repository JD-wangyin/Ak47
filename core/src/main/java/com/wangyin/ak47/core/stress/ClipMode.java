package com.wangyin.ak47.core.stress;


/**
 * 多种模式：
 *  - DEFAULT           按装弹顺序取出，子弹总会打光的
 *  - REPEAT            重复取，子弹无限
 *  - REPEAT_ONE        重复一颗子弹（第一颗），子弹无限
 *  - SHUFFLE           随机取子弹，子弹无限
 *  
 * 
 * 
 * @author wyhanyu
 *
 */
public enum ClipMode {
    
    DEFAULT,
    
    REPEAT,
    
    REPEAT_ONE,
    
    SHUFFLE;
    
    

}
