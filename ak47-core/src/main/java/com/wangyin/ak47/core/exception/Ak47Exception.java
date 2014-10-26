package com.wangyin.ak47.core.exception;


/**
 * An {@link Exception} which is thrown by a ak47-core.
 * 
 * @author hannyu
 *
 */
public class Ak47Exception extends Exception {
    
    private static final long serialVersionUID = -2400918594219866598L;
    
    public Ak47Exception(){
        super();
    }
    public Ak47Exception(String msg) {
        super(msg);
    }
    public Ak47Exception(String message, Throwable cause) {
        super(message, cause);
    }
    public Ak47Exception(Throwable cause) {
        super(cause);
    }



    
}
