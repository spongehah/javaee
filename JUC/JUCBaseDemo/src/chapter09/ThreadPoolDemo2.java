package chapter09;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 * 开发中前面讲的三种线程池创建方式都不会用，因为允许的请求队列数量是Integer.Max，会造成OOM
 * 所以一般使用自定义线程池
 */
//自定义线程池创建
public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                3L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),    //阻塞队列
                Executors.defaultThreadFactory(),       //线程工厂
                new ThreadPoolExecutor.AbortPolicy()    //拒绝策略
        );

        try {
            //10个顾客请求
            for (int i = 0; i < 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "办理事务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

}
