package chapter02.demo1_synchronized;

//第一步：创建资源类
class Ticket {
    //票数
    private int number = 30;
    
    private int saleCount = 0;
    
    //操作方法：卖票
    public synchronized void sale(){
        if (number > 0){
            System.out.println(Thread.currentThread().getName() + "卖出第" + (++saleCount) + "张票，剩余：" + --number);
        }
    }
}


public class SaleTicket {

    //第二步：创建多个线程，调用资源类的操作方法
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        
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



