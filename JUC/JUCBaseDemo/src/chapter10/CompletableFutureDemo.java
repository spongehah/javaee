package chapter10;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步调用和同步调用
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //没有返回值的异步调用
        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "completableFuture1");  
        });
        completableFuture1.get();
        
        //由返回值的异步调用
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "completableFuture2");
            //模拟异常
            int i = 1/0;
            return 1024;
        });
        completableFuture2.whenComplete((t,u) -> {
            System.out.println("-------t=" + t);
            System.out.println("-------u=" + u);
        }).get();
    }
}
