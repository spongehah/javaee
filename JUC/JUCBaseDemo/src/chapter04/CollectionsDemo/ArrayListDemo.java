package chapter04.CollectionsDemo;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ArrayList是线程不安全的，因为它的add()等方法并没有使用synchronized关键字
 * 
 * 会报异常：java.util.ConcurrentModificationException   并发修改问题
 * 
 * 解决方案：1 Vector                     线程安全，但古老，效率低，不常用
 *         2 Collections                古老，不常用
 *         3 CopyOnWriteArrayList       JUC工具包中的类，常用
 */
public class ArrayListDemo {
    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
        
        //方法一：Vector
//        List<String> list = new Vector<>();

        //方法二：Collections工具类、
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        
        //方法三：CopyOnWriteArrayList      写时复制技术
        List<String> list = new CopyOnWriteArrayList<>();
        
        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(UUID.randomUUID().toString().substring(0,8));
                    System.out.println(list);
                }
            },String.valueOf(i)).start();
        }
    }
}
    