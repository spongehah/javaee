package chapter06;

import java.util.concurrent.CountDownLatch;

/**
 * 演示JUC辅助类：CountDownLatch
 * await()，阻塞线程，等待计数器为0后才执行后面的语句
 * countDown()，每次对计数器  -1
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        //6位同学，都离开教室后，班长才锁门
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "离开");
                
                //计数器 -1
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        //阻塞线程等待计数器为0
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "班长锁门走人了");
        
    }
}
