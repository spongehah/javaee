package com.spongehah.juc.CompletableFuture;

import org.junit.Test;

import java.util.concurrent.*;

public class CompletableFutureAPI2Demo {

    public static void main(String[] args) {
        /**
         * thenApply演示
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println("111");
            return 1;
        },threadPool).thenApply(f -> {
            System.out.println("222");
            return f + 2;
        }).thenApply(f -> {
            System.out.println("333");
            return f + 3;
        }).whenComplete((v,e) -> {
            if(e == null){
                System.out.println("----计算结果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("异常： " + e.getCause() + "\t" + e.getMessage());
            return null;
        });
        threadPool.shutdown();
    }
    
    @Test
    public void test(){
        /**
         * handle演示
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println("111");
            return 1;
        },threadPool).handle((f,e) -> {
            int i = 10/0;
            System.out.println("222");
            return f + 2;
        }).handle((f,e) -> {
            System.out.println("333");
            return f + 3;
        }).whenComplete((v,e) -> {
            if(e == null){
                System.out.println("----计算结果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("异常： " + e.getCause() + "\t" + e.getMessage());
            return null;
        });
        threadPool.shutdown();
    }
}
