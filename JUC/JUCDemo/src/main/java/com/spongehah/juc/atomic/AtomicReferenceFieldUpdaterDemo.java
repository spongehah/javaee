package com.spongehah.juc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 系统初始化案例
 */
class MyVar{
    public volatile Boolean isInit = Boolean.FALSE;
    
    AtomicReferenceFieldUpdater<MyVar,Boolean> fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyVar.class,Boolean.class,"isInit");
    public void init(){
        if (fieldUpdater.compareAndSet(this,Boolean.FALSE,Boolean.TRUE)){
            System.out.println(Thread.currentThread().getName() + "\t------------start init,needs 2 seconds");
            try {TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t------------init over");
        }else{
            System.out.println(Thread.currentThread().getName() + "\t----------- already have other thread is initing");
        }
    }
}
public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        MyVar myVar = new MyVar();
        for (int i = 0; i < 5; i++) {
            new Thread(myVar::init,String.valueOf(i)).start();
        }
    }
}
