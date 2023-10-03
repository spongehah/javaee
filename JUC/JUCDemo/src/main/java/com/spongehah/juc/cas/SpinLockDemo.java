package com.spongehah.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自写一个自旋锁
 */
public class SpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    
    public void lock(){
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "------come in");
        while (!atomicReference.compareAndSet(null,thread)){
            
        }
        System.out.println(Thread.currentThread().getName() + "-------lock");
    }
    
    public void unlock(){
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(thread,null)){
            
        }
        System.out.println(Thread.currentThread().getName() + "---------task over  ,unlock");
    }

    public static void main(String[] args) {
        SpinLockDemo lockDemo = new SpinLockDemo();
        
        new Thread(() -> {
            lockDemo.lock();
            //暂停几秒钟线程
            try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            lockDemo.unlock();
        },"A").start();
        
        //暂停500ms，让线程A先启动
        try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
        
        new Thread(() -> {
            lockDemo.lock();
            
            lockDemo.unlock();
        },"B").start();
    }
}
