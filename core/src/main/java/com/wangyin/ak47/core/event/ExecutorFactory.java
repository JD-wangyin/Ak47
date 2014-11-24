package com.wangyin.ak47.core.event;

import java.util.concurrent.Executor;

public interface ExecutorFactory {
    
    public Executor newExecutor();

}
