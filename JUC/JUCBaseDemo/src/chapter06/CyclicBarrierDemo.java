package chapter06;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * JUC辅助类CyclicBarrier演示：
 * 理解：循环栅栏
 * new CyclicBarrier(int parties,Runnable barrierAction)
 * await()：当调用 parties 次await()方法后，将执行barrierAction中定义的语句
 * 若未达到parties次调用，则一直阻塞barrierAction的调用
 */
public class CyclicBarrierDemo {
    
    public static final int NUMBER = 7;
    
    //集齐7颗龙珠才能召唤神龙
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER,() -> {
            System.out.println(Thread.currentThread().getName() + "成功召唤神龙");
        });


        for (int i = 1; i <= 7; i++) {
            new Thread(() -> {
                try {
                    System.out.println("成功收集到" + Thread.currentThread().getName() + "星龙珠");
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
