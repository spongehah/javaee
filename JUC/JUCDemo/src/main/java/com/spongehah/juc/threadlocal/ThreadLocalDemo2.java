package com.spongehah.juc.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 【强制】必须回收自定义的 ThreadLocal 变量，尤其在线程池场景下，线程经常会被复用，如果不清理
 * 自定义的 ThreadLocal 变量，可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用
 * try-finally 块进行回收。
 */
class MyData{
    ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
    public void add(){
        threadLocal.set(1 + threadLocal.get());
    }
}
public class ThreadLocalDemo2 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        MyData myData = new MyData();
        try {
            for (int i = 1; i <= 10 ; i++) {
                threadPool.submit(() -> {
                    try {
                        Integer beforeInt = myData.threadLocal.get();
                        myData.add();
                        Integer afterInt = myData.threadLocal.get();
                        System.out.println(Thread.currentThread().getName()+"\t"+"beforeInt:"+beforeInt+"\t afterInt: "+afterInt);
                    } finally {
                        myData.threadLocal.remove();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
