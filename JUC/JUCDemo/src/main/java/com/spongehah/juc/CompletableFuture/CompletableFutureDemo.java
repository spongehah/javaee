package com.spongehah.juc.CompletableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 演示FutureTask + Callable接口的入门调用
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("-----------come in call()");
                return "hello Callable";
            }
        });
        
        new Thread(futureTask,"t1").start();

        //调用get()方法获取返回值
        System.out.println(futureTask.get());
    }
}
