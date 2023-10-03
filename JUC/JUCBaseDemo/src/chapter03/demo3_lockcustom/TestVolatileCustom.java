package chapter03.demo3_lockcustom;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程间定制化通信
 * 
 * 这里实现：
 * flag=1时，aa打印5次，并set flag=2
 * flag=2时，bb打印10次，并set flag=3
 * flag=3时，cc打印15次，并set flag=1
 * 循环10次
 */
//第一步：创建资源类
class ShareResource {
    //标志位
    private int flag = 1;

    //创建可重入锁
    private final Lock lock = new ReentrantLock();
    //创建三个condition，实现指定唤醒
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    
    //打印5次
    public void print5(int loop) throws InterruptedException {

        //第二步：判断 干活 通知
        lock.lock();
        try {
            //判断
            while (flag != 1){
                c1.await();
            }

            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + + i + "，轮数：" + loop);
                
                //修改标志位
                flag = 2;
                c2.signal();
            }
        }finally {
            lock.unlock();
        }
    }

    //打印10次
    public void print10(int loop) throws InterruptedException {

        //第二步：判断 干活 通知
        lock.lock();
        try {
            //判断
            while (flag != 2){
                c2.await();
            }

            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + + i + "，轮数：" + loop);

                //修改标志位
                flag = 3;
                c3.signal();
            }
        }finally {
            lock.unlock();
        }
    }

    //打印15次
    public void print15(int loop) throws InterruptedException {

        //第二步：判断 干活 通知
        lock.lock();
        try {
            //判断
            while (flag != 3){
                c3.await();
            }

            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + + i + "，轮数：" + loop);

                //修改标志位
                flag = 1;
                c1.signal();
            }
        }finally {
            lock.unlock();
        }
    }
    
}

public class TestVolatileCustom {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <10; i++) {
                    try {
                        shareResource.print5(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"aa").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <10; i++) {
                    try {
                        shareResource.print10(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        },"bb").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <10; i++) {
                    try {
                        shareResource.print15(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"cc").start();
    }
}
