package com.spongehah.juc.LockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示阻塞和唤醒线程的三种方式
 */
public class LockSupportDemo
{
    static int x = 0;
    static int y = 0;

    /**
     * LockSupport的park()演示：
     * 解决其它两个办法的问题
     * 但是：
     *  凭证最多只能有1个，累加无效，只能调用一对park()和unpark()
     *  在同步方法中时，park()不会释放锁，而上面方法的wait()是会释放锁的
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t ----come in"+System.currentTimeMillis());
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t ----被唤醒"+System.currentTimeMillis());
        }, "t1");
        t1.start();

        //暂停几秒钟线程
        //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName()+"\t ----发出通知");
        },"t2").start();
    }

    /**
     * Condition的await()演示：
     * 1 在同步代码块中先await再signal：下程序正常运行
     * 2 没有同步代码块：报异常
     * 3 在同步代码块中先ignal再await：await线程不能被唤醒
     */
    private static void lockAwaitSignal() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t ----come in");
                condition.await();
                System.out.println(Thread.currentThread().getName()+"\t ----被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            lock.lock();
            try
            {
                condition.signal();
                System.out.println(Thread.currentThread().getName()+"\t ----发出通知");
            }finally {
                lock.unlock();
            }
        },"t2").start();
    }

    /**
     * Object的wait()演示：
     * 1 在同步代码块中先wait再notify：下程序正常运行
     * 2 没有同步代码块：报异常
     * 3 在同步代码块中先notify再wait：wait线程不能被唤醒
     */
    private static void syncWaitNotify() {
        Object objectLock = new Object();

        new Thread(() -> {
            //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            synchronized (objectLock){
                System.out.println(Thread.currentThread().getName()+"\t ----come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t ----被唤醒");
            }
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            synchronized (objectLock){
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+"\t ----发出通知");
            }
        },"t2").start();
    }
}
