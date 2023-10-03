package com.spongehah.juc.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 需求：
 * 10个线程，
 * 每个线程转账1000，
 * 不使用synchronized,尝试使用AtomicIntegerFieldUpdater来实现。
 */
class BankAccount {
    String bankName = "CCB";
//    int money = 0;

    //AtomicIntegerFieldUpdater更新的对象属性必须使用 public volatile 修饰符。
    public volatile int money = 0;
    
    //普通synchronize方法
    public synchronized void add(){
        money++;
    }

    //因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
    AtomicIntegerFieldUpdater<BankAccount> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class,"money");
    
    //不加synchronized，保证高性能原子性，局部微创小手术
    public void transMoney(){
        fieldUpdater.getAndIncrement(this);
    }
    
}
public class AtomicIntegerFieldUpdaterDemo {
    public static void main(String[] args) throws InterruptedException {
        BankAccount bankAccount = new BankAccount();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        //bankAccount.add();
                        bankAccount.transMoney();      //使用属性修改器
                    }
                } finally {
                    countDownLatch.countDown();
                }
            },String.valueOf(i)).start();
        }
        
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\tresult: " + bankAccount.money);
    }
}
