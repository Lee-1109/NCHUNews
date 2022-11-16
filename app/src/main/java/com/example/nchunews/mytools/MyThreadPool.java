package com.example.nchunews.mytools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool extends ThreadPoolExecutor {
    private static final int coreSize = 5;
    private static final int maxPoolSize = 20;
    private static final int keepAlive = 20;
    private static final TimeUnit unit = TimeUnit.SECONDS;
    private static  BlockingQueue<Runnable> workQueue= new LinkedBlockingQueue<Runnable>(100);
    public MyThreadPool(){
        super(coreSize, maxPoolSize,keepAlive ,unit, workQueue);
    }
}
