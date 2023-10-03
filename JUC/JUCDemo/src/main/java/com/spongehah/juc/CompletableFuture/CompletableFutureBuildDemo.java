package com.spongehah.juc.CompletableFuture;

import java.util.concurrent.*;

/**
 * 演示CompletableFuture创建实例的四种方式
 */
public class CompletableFutureBuildDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        
        //无返回值使用默认线程池   ForkJoinPool.commonPool
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println(completableFuture.get());


        //无返回值使用指定线程池
        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
            try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName());
        },threadPool);
        System.out.println(completableFuture1.get());

        //有返回值使用默认线程池   ForkJoinPool.commonPool
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName());
            return "hello supply";
        });
        System.out.println(completableFuture2.get());

        //有返回值使用指定线程池
        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName());
            return "hello supply";
        },threadPool);
        System.out.println(completableFuture3.get());

    }
}
