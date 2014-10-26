package com.wangyin.ak47.core;

public interface Promise<O, I> extends Future<O, I> {

    Promise<O, I> setSuccess();

    boolean trySuccess();

    Promise<O, I> setFailure(Throwable cause);
    
    boolean tryFailure(Throwable cause);
    
    Promise<O, I> await() throws InterruptedException;
    
    Promise<O, I> awaitUninterruptibly();
    
    Promise<O, I> sync() throws InterruptedException;

    Promise<O, I> syncUninterruptibly();
}
