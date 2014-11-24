package com.wangyin.ak47.core.exception;


/**
 * An {@link RuntimeException} which is thrown by a ak47-core.
 * 
 * @author hannyu
 *
 */
public class Ak47RuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = -2400918594219866598L;
    
    public Ak47RuntimeException(){
        super();
    }
    public Ak47RuntimeException(String msg) {
        super(msg);
    }
    public Ak47RuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    public Ak47RuntimeException(Throwable cause) {
        super(cause);
    }



    
}
