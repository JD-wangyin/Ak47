package com.wangyin.ak47.core;


import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.util.RequestAttr;
import com.wangyin.ak47.core.util.SessionAttr;



/**
 * Message is the entity passed through Handlers.
 * 
 * It contains two parts:
 *  - POJO          Human Readable data structure
 *  - Buffer        Binary bytes buffer
 *  
 * The relationship between Message/Request/Response/POJO/Buffer:
 * 
 *    +--------+      decode      +--------+
 *    |        |  ------------->  |        |
 *    | Buffer |                  |  POJO  |
 *    |        |  <-------------  |        |
 *    +--------+      encode      +--------+
 *        ^                         ^  ^
 *        |                         |  |
 *        |            has          |  |
 *    has |  +----------------------+  |
 *        |  |                         | has
 *        |  |                         |
 *    +---------+                +-----------+ 
 *    |         |                |  Request  | 
 *    | Message |                +-----------+ 
 *    |         |                |  Response | 
 *    +---------+                +-----------+ 
 *    (in handler)               (in service)
 * 
 * 
 * Message代表Handler中传递的消息，它包含两部分：
 *  - POJO          上层可读的结构化数据
 *  - Buffer        二进制数据缓存
 *  
 * 
 * @author hannyu
 *
 * @param <T>           POJO
 */
public interface Message<T> { 
    
    /**
     * Contains POJO or not?
     * 
     * @return
     */
    public boolean hasPojo();
    
    
    /**
     * Get POJO
     * 
     * @return  null if not contains POJO
     */
    public T getPojo();
    
    
    /**
     * Set POJO
     * 
     * @param t
     */
    public void setPojo(T pojo);
    
    
    /**
     * Contains Buffer or not?
     * 
     * @return
     */
    public boolean hasBuffer();
    
    /**
     * Get Buffer
     * 
     * @return  null if not contains Buffer
     */
    public Buffer getBuffer();
    
    
    public void clear();
    
    public RequestAttr getRequestAttr();
    
    public SessionAttr getSessionAttr();
    
    public <K> Message<K> newMessage();
    
}
