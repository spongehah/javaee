package chapter04.SyncLockDemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncLockDemo {
    /**
     * 公平锁
     */
    //创建可重入锁
    private final ReentrantLock lock = new ReentrantLock(false);       //设置是否为公平锁，默认为false，代表抢占式锁，若为true，则表示若有人，则自觉排队


    public synchronized void add() {
        add();
    }

    public static void main(String[] args) {
        /**
         * 可重入锁（递归锁）：多层锁机制只需要同一把锁即可自由出入
         * synchronized(隐式)    lock(显式)
         */
        //synchronized可重入锁演示1：
        /*Object o = new Object();
        new Thread(() -> {
            synchronized (o){
                System.out.println(Thread.currentThread().getName() + "外层");

                synchronized (o){
                    System.out.println(Thread.currentThread().getName() + "中层");

                    synchronized (o){
                        System.out.println(Thread.currentThread().getName() + "内层");
                    }
                }
            }
        },"t1").start();*/


        //synchronized可重入锁演示2：
        new SyncLockDemo().add();   //将会报栈内存溢出，证明可以递归调用add方法自己，是可重入锁
    }

    /**
     * lock演示可重入锁       递归使用锁必须一上锁对应一解锁，加入内部锁上锁了不解锁，自己线程使用会没问题，但是会影响其它线程
     */
    @Test
    public void testLock() {
        Lock lock = new ReentrantLock();
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 外层");

                try {
                    //上锁
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " 内层");
                } finally {
                    //释放锁
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "t1").start();

    }
}

