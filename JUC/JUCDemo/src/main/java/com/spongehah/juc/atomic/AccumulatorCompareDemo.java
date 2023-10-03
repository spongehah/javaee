package com.spongehah.juc.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * i++点赞各实现方法比较
 */
class ClickNumber{
    /**
     * 1 使用synchronized
     */
    long number = 0;
    public synchronized void clickBySynchronized(){
        number++;
    }
    /**
     * 2 使用AtomicLong
     */
    AtomicLong atomicLong = new AtomicLong(0);
    public void clickByAtomicLong(){
        atomicLong.getAndIncrement();
    }
    /**
     * 3 使用AtomicLongFieldUpdater
     */
    public volatile long number2 = 0;
    AtomicLongFieldUpdater<ClickNumber> fieldUpdater = AtomicLongFieldUpdater.newUpdater(ClickNumber.class,"number2");
    public void clickByAtomicLongFieldUpdater(){
        fieldUpdater.getAndIncrement(this);
    }
    /**
     * 4 使用LongAdder
     */
    LongAdder longAdder = new LongAdder();
    public void clickByLongAdder(){
        longAdder.increment();
    }
    /**
     * 5 使用LongAccumulator
     */
    LongAccumulator longAccumulator = new LongAccumulator((x,y) -> x + y,0);
    public void clickByLongAccumulator(){
        longAccumulator.accumulate(1);
    }
}
public class AccumulatorCompareDemo {
    public static final int _1W = 10000;
    public static final int threadNumber = 50;
    public static void main(String[] args) throws InterruptedException {
        ClickNumber clickNumber = new ClickNumber();
        long startTime;
        long endTime;

        CountDownLatch countDownLatch1 = new CountDownLatch(threadNumber);
        CountDownLatch countDownLatch2 = new CountDownLatch(threadNumber);
        CountDownLatch countDownLatch3 = new CountDownLatch(threadNumber);
        CountDownLatch countDownLatch4 = new CountDownLatch(threadNumber);
        CountDownLatch countDownLatch5 = new CountDownLatch(threadNumber);

        startTime = System.currentTimeMillis();
        for (int i = 1; i <=threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <=100 * _1W; j++) {
                        clickNumber.clickBySynchronized();
                    }
                } finally {
                    countDownLatch1.countDown();
                }
            },String.valueOf(i)).start();
        }
        countDownLatch1.await();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t clickBySynchronized: "+clickNumber.number);

        startTime = System.currentTimeMillis();
        for (int i = 1; i <= threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 100 * _1W; j++){
                        clickNumber.clickByAtomicLong();
                    }
                } finally {
                    countDownLatch2.countDown();
                }
            },String.valueOf(i)).start();;
        }
        countDownLatch2.await();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t clickByAtomicLong: "+clickNumber.atomicLong.get());

        startTime = System.currentTimeMillis();
        for (int i = 1; i <= threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 100 * _1W; j++){
                        clickNumber.clickByAtomicLongFieldUpdater();
                    }
                } finally {
                    countDownLatch3.countDown();
                }
            },String.valueOf(i)).start();;
        }
        countDownLatch3.await();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t clickByAtomicLongFieldUpdater: "+clickNumber.number2);

        startTime = System.currentTimeMillis();
        for (int i = 1; i <= threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 100 * _1W; j++){
                        clickNumber.clickByLongAdder();
                    }
                } finally {
                    countDownLatch4.countDown();
                }
            },String.valueOf(i)).start();;
        }
        countDownLatch4.await();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t clickByLongAdder: "+clickNumber.longAdder.sum());

        startTime = System.currentTimeMillis();
        for (int i = 1; i <= threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 100 * _1W; j++){
                        clickNumber.clickByLongAccumulator();
                    }
                } finally {
                    countDownLatch5.countDown();
                }
            },String.valueOf(i)).start();;
        }
        countDownLatch5.await();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t clickByLongAccumulator: "+clickNumber.longAccumulator.get());
    }
}
