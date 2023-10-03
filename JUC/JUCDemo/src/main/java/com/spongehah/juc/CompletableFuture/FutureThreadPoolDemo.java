package com.spongehah.juc.CompletableFuture;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * Future优点： + ThreadPool异步任务耗时低演示
 */
public class FutureThreadPoolDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        //三个线程完成三个任务的耗时（即异步任务）
        //不调用get()：353 毫秒
        //调用get()： 865 毫秒

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        
        long startTime = System.currentTimeMillis();
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            return "task1 over";
        });
        threadPool.submit(futureTask1);

        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            try {TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
            return "task2 over";
        });
        threadPool.submit(futureTask2);

//        System.out.println(futureTask1.get());
//        System.out.println(futureTask2.get());

        try {TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
        
        long endTime = System.currentTimeMillis();
        System.out.println("----costTime：" + (endTime - startTime) + " 毫秒");

        
        System.out.println(Thread.currentThread().getName() + "\t ----end");
        threadPool.shutdown();
        
    }
    
    @Test
    public void m1(){
        //一个线程完成三个任务的耗时     耗时1125 毫秒
        long startTime = System.currentTimeMillis();
        try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
        try {TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
        try {TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}

        long endTime = System.currentTimeMillis();
        System.out.println("----costTime：" + (endTime - startTime) + " 毫秒");

        System.out.println(Thread.currentThread().getName() + "\t ----end");
    }
}
