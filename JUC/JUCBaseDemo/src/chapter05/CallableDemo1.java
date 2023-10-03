package chapter05;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 通过Callable接口创建线程 ：可以有返回值 jdk1.5 新增
 */

/**
 * 比较Runnable和Callable接口
 * 
 * Runnable     run()       无返回值        不抛出异常       创建方式不同
 * Callable     call()      有返回值        会抛出异常       创建方式不同
 */

//实现Runnable：
class MyThread1 implements Runnable{
    @Override
    public void run() {
        
    }
}

//实现Callable：
class MyThread2 implements Callable{
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName()+" come in callable");
        return 200;
    }
}

public class CallableDemo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建Runnable线程
        new Thread(new MyThread1(),"AA").start();
        
        //创建Callable线程  报错
//        new Thread(new MyThread2(),"BB").start();
        
        //创建FutureTask
        FutureTask<Integer> futureTask1 = new FutureTask<>(new MyThread2());
        
        //lambda表达式
        FutureTask<Integer> futureTask2 = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName()+" come in callable");
            return 1024;
        });
        
        
        //创建Callable线程:
        new Thread(futureTask2,"Lucy").start();
        new Thread(futureTask1,"mary").start();
        
        //调用FutureTask的get()方法得到Callable接口的返回值
        System.out.println(futureTask2.get());

        System.out.println(futureTask1.get());

        System.out.println(Thread.currentThread().getName()+" come over");
        
        //FutureTask原理  未来任务
        /**
         * 1、老师上课，口渴了，去买票不合适，讲课线程继续。
         *   单开启线程找班上班长帮我买水，把水买回来，需要时候直接get
         *
         * 2、4个同学，   1同学 1+2...5   ，  2同学 10+11+12....50， 3同学 60+61+62，  4同学 100+200
         *      第2个同学计算量比较大，
         *     FutureTask单开启线程给2同学计算，先汇总 1 3 4 ，最后等2同学计算位完成，统一汇总
         *
         * 3、考试，做会做的题目，最后看不会做的题目
         *
         * 汇总一次
         *
         */
    }
}
