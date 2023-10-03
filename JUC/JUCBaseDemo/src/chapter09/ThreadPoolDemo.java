package chapter09;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池      创建多线程的第四种办法
 */
public class ThreadPoolDemo {
    //演示线程池三种常用分类
    public static void main(String[] args) {

        //一池五线程
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);

        //一池一线程
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();

        //一池可扩容线程
        ExecutorService threadPool3 = Executors.newCachedThreadPool();

        try {
            //10个顾客请求
            for (int i = 1; i <= 20; i++){
                //执行
                threadPool3.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " 办理事务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程池
            threadPool3.shutdown();
        }

    }
}
