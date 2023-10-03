package chapter04.CollectionsDemo;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * HashSet也是线程不安全的
 * 
 * 也会出现并发修改问题   java.util.ConcurrentModificationException
 * 
 * 解决方法：CopyOnWriteArraySet
 */
public class HashSetDemo {

    public static void main(String[] args) {
//        Set<String> set = new HashSet<>();

        //解决方法：CopyOnWriteArraySet
        Set<String> set = new CopyOnWriteArraySet<>();
        
        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    set.add(UUID.randomUUID().toString().substring(0,8));
                    System.out.println(set);
                }
            },String.valueOf(i)).start();
        }
    }
}
