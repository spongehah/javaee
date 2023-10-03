package chapter03.demo1_synchronized;

/**
 * synchronized测试线程间通信：交替加减
 */

//第一步：创建资源类
class Share {
    //初始值
    private int number = 0;
    //+1方法
    public synchronized void incr() throws InterruptedException {
        //第二步：判断 干活 通知
        while (number != 0){   //判断 number是否为0，如果不是0，等待
            this.wait();       //不能使用if，而要使用while避免虚假唤醒，因为wait从哪里睡，从哪里醒，醒了过后会继续往下执行
        }
        //如果number是0，+1
        number++;
        System.out.println(Thread.currentThread().getName() + "::" + + number);
        //通知 其它线程
        this.notifyAll();
    }
    
    //-1方法
    public synchronized void decr() throws InterruptedException {
        //第二步：判断 干活 通知
        while (number != 1){   //判断 number是否为1，如果不是1，等待
            this.wait();
        }
        //如果number是1，-1
        number--;
        System.out.println(Thread.currentThread().getName() + "::" + + number);
        //通知 其它线程
        this.notifyAll();
    }
}


public class TestVolatile {
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
