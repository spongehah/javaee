package com.spongehah.juc.CompletableFuture;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * CompletableFuture异步任务基本使用演示
 */
public class CompletableFutureUsesDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "----come in");
                int result = ThreadLocalRandom.current().nextInt(10);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result > 2){
                    int i = 10/0;
                }
                System.out.println("----1秒后出结果" + result);
                return result;
            },threadPool).whenComplete((v,e) -> {
                if (e == null){
                    System.out.println("-----计算完成，更新系统UpdateValue："+v);
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                System.out.println("异常情况：" + e.getCause() + "\t" + e.getMessage());
                return null;
            });

            System.out.println(Thread.currentThread().getName() + "主线程先去忙其它任务了");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

        //若使用默认线程池主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:暂停3秒钟线程
        //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

    }


    //演示CompletableFuture完成Future接口的任务
    @Test
    public void testFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "----come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("----1秒后出结果" + result);
            return result;
        });

        System.out.println(Thread.currentThread().getName() + "主线程先去忙其它任务了");

        System.out.println(completableFuture.get());
    }
}
