package chapter02.demo2_lock;

import java.util.concurrent.locks.ReentrantLock;

//第一步：创建资源类
class LTicket {
    //票数
    private int number = 30;

    private int saleCount = 0;

    //创建可重入锁
    private final ReentrantLock lock = new ReentrantLock();
    
    //操作方法：卖票
    public void sale(){
        
        //上锁
        lock.lock();
        try {
            if (number > 0){
                System.out.println(Thread.currentThread().getName() + "卖出第" + (++saleCount) + "张票，剩余：" + --number);
            }
        } finally {
            //释放锁
            lock.unlock();
        }
    }
}

public class LSaleTicket {
    //第二步：创建多个线程，调用资源类的操作方法
    public static void main(String[] args) {
        LTicket ticket = new LTicket();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <40; i++) {
                    ticket.sale();
                    try {
                        Thread.sleep(100);
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
                    ticket.sale();
                    try {
                        Thread.sleep(100);
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
                    ticket.sale();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"cc").start();
    }
}
