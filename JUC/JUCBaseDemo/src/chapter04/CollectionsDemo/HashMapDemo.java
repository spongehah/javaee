package chapter04.CollectionsDemo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HashMap也是线程不安全的
 *
 * 也会出现并发修改问题   java.util.ConcurrentModificationException
 *
 * 解决方法：1 HashTable                 不推荐
 *         2 ConcurrentHashMap         推荐
 */
public class HashMapDemo {

    public static void main(String[] args) {
//        Map<String,String> map = new HashMap<>();

        //解决方法：ConcurrentHashMap
        Map<String,String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 30; i++) {
            String key = String.valueOf(i);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    map.put(key,UUID.randomUUID().toString().substring(0,8));
                    System.out.println(map);
                }
            },String.valueOf(i)).start();
        }
    }
}
