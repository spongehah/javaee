package chapter03.demo2_lock;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock测试线程间通信：交替加减
 */
//第一步：创建资源类
class Share {
    //初始值
    private int number = 0;
    
    //创建可重入锁
    private final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    
    //+1方法
    public void incr() throws InterruptedException {
        //第二步：判断 干活 通知
        
        lock.lock();
        try {
            while (number != 0){   //判断 number是否为0，如果不是0，等待
                condition.await();       //不能使用if，而要使用while避免虚假唤醒，因为wait从哪里睡，从哪里醒，醒了过后会继续往下执行
            }
            //如果number是0，+1
            number++;
            System.out.println(Thread.currentThread().getName() + "::" + + number);
            //通知 其它线程
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    //-1方法
    public void decr() throws InterruptedException {
        //第二步：判断 干活 通知
        
        lock.lock();
        try {
            while (number != 1){   //判断 number是否为1，如果不是1，等待
                condition.await();
            }
            //如果number是1，-1
            number--;
            System.out.println(Thread.currentThread().getName() + "::" + + number);
            //通知 其它线程
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}


public class LTestVolatile {
    //第三步：创建多个线程，调用资源类的操作方法
    public static void main(String[] args) {

        Share share = new Share();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <40; i++) {
                    try {
                        share.incr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"aa").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <40; i++) {
                    try {
                        share.decr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        },"bb").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <40; i++) {
                    try {
                        share.incr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"cc").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <40; i++) {
                    try {
                        share.decr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        },"dd").start();
    }
}
