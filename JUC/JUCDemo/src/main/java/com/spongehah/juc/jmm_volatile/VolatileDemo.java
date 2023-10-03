package com.spongehah.juc.jmm_volatile;

import java.util.concurrent.TimeUnit;

/**
 * 为什么volatile关键字不满足原子性演示
 */
class MyNumber {
    //共享数据，是存储在共享主内存中的
    volatile int number = 0;

    public void addNumber(){
        number++;
    }
}

public class VolatileDemo {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myNumber.addNumber();
                }
            },String.valueOf(i)).start();
        }
        
        try {
            TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}

        System.out.println(myNumber.number);
    }
}
