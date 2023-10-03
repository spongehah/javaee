package com.spongehah.juc.CompletableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 缺点演示：1 get()容易导致线程阻塞
 *         2 isDone()轮询消耗CPU
 */
public class FutureAPIDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "take over");
            return "take over";
        });
        
        new Thread(futureTask,"t1").start();

//        futureTask.get();   //会阻塞下面main线程的语句执行
        futureTask.get(3,TimeUnit.SECONDS); //可设置等待时间
        
        System.out.println(Thread.currentThread().getName() + "正在执行其它任务中");
        
        while (true){
            if (futureTask.isDone()){
                System.out.println(futureTask.get());
                break;
            }else {
                try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
                System.out.println("正在等待get中");
            }
        }
    }
}
