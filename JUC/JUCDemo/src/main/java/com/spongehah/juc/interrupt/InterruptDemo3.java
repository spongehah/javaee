package com.spongehah.juc.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 1 中断标志位，默认false
 * 2 t2 ----> t1发出了中断协商，t2调用t1.interrupt()，中断标志位true
 * 3 中断标志位true，正常情况，程序停止，^_^
 * 4 中断标志位true，异常情况，InterruptedException，将会把中断状态将被清除，并且将收到InterruptedException 。中断标志位false
 *    导致无限循环
 *
 * 5 在catch块中，需要再次给中断标志位设置为true，2次调用停止程序才OK
 */
public class InterruptDemo3 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t " +
                            "中断标志位：" + Thread.currentThread().isInterrupted() + " 程序停止");
                    break;
                }
                //设置线程为阻塞状态，即上述第4点
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();//需要再次自己在catch中调用interrupt()，否则无限循环
                    e.printStackTrace();
                }
                System.out.println("----------hello InterruptDemo3");
            }
        }, "t1");
        
        t1.start();
        
        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
        
        new Thread(() -> t1.interrupt(),"t2").start();
    }
}