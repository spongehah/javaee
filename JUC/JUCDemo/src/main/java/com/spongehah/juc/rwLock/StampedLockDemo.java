package com.spongehah.juc.rwLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {
    static int number = 37;
    static StampedLock stampedLock = new StampedLock();
    public void write(){
        long stamped = stampedLock.writeLock();
        System.out.println(Thread.currentThread().getName()+"\t"+"写线程准备修改");
        try {
            number = number + 13;
        }finally {
            stampedLock.unlockWrite(stamped);
        }
        System.out.println(Thread.currentThread().getName()+"\t"+"写线程结束修改");
    }
    //悲观读，读没有完成时候写锁无法获得锁
    public void read(){
        long stamped = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName()+"\t"+" come in readlock code block，4 seconds continue...");
        for (int i = 0; i < 4; i++) {
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName()+"\t"+" 正在读取中......");
        }
        int result = number;
        try {
            System.out.println(Thread.currentThread().getName()+"\t"+" 获得成员变量值result："+result);
            System.out.println("写线程没有修改成功，读锁时候写锁无法介入，传统的读写互斥");
            System.out.println(Thread.currentThread().getName() + "\t 读线程结束");
        } finally {
            stampedLock.unlockRead(stamped);
        }
        System.out.println(Thread.currentThread().getName()+"\t"+" finally value: "+result);
    }

    //乐观读，读的过程中也允许获取写锁介入
    public void tryOptimisticRead(){
        long stamped = stampedLock.tryOptimisticRead();
        int result = number;
        //故意间隔4秒钟，很乐观认为读取中没有其它线程修改过number值，具体靠判断
        System.out.println("4秒前stampedLock.validate方法值(true无修改，false有修改)"+"\t"+stampedLock.validate(stamped));
        for (int i = 0; i < 4; i++) {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t"+"正在读取... "+i+" 秒" +
                    "后stampedLock.validate方法值(true无修改，false有修改)"+"\t"+stampedLock.validate(stamped));
        }
        if (!stampedLock.validate(stamped)){
            System.out.println("有人修改过------有写操作");
            stamped = stampedLock.readLock();
            try {
                System.out.println("从乐观读 升级为 悲观读");
                result = number;
                System.out.println("重新悲观读后result："+result);
            }finally {
                stampedLock.unlockRead(stamped);
            }
        }
        System.out.println(Thread.currentThread().getName()+"\t"+" finally value: "+result);
    }

    public static void main(String[] args) {
        StampedLockDemo resource = new StampedLockDemo();
        /**
         * 传统版
         */
        /*new Thread(resource::read,"readThread").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"----come in");
            resource.write();
        },"writeThread").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(Thread.currentThread().getName()+"\t"+"number:" +number);*/

        /**
         * 乐观版
         */
        new Thread(resource::tryOptimisticRead,"readThread").start();

        //暂停6秒钟线程
        try { TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e) { e.printStackTrace(); }
        
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"----come in");
            resource.write();
        },"writeThread").start();
    }
}
