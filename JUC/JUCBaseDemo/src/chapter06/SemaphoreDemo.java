package chapter06;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 演示JUC辅助类：SemaphoreDemo:
 * new Semaphore(3);设置许可数量permits
 * acquire()：抢占许可
 * release()：释放许可
 */
public class SemaphoreDemo {

    //6辆车抢3个车位
    public static void main(String[] args) {

        //创建semaphore，设置许可数量
        Semaphore semaphore = new Semaphore(3);

        //模拟6辆汽车
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {

                try {
                    //抢占
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "抢到了车位");

                    //设置随机停车时间
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    System.out.println(Thread.currentThread().getName() + "---------离开了车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}


