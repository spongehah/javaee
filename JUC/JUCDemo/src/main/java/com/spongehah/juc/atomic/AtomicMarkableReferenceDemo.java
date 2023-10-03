package com.spongehah.juc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceDemo {
    public static void main(String[] args) {
        AtomicMarkableReference<Integer> markableReference = new AtomicMarkableReference<>(100,false);
        
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.isMarked());
            //保证t2线程也拿到和我相同的mark值
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            markableReference.compareAndSet(100,1000,markableReference.isMarked(),!markableReference.isMarked());
        },"t1").start();
        
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.isMarked());
            //保证t1先修改
            try {TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
            boolean b = markableReference.compareAndSet(100,2000,markableReference.isMarked(),!markableReference.isMarked());
            System.out.println(Thread.currentThread().getName() + "修改结果：" + b);
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.isMarked());
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.getReference());
        },"t2").start();
    }
}
