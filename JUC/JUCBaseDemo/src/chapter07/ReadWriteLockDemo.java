package chapter07;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：ReadWriteLock
 * 读锁：共享锁   会发生死锁
 * 写锁：独占锁   会发生死锁
 * 读的时候不能写，写的时候可以读
 */
//新建资源类
class MyCache{
    //创建map集合
    private volatile Map<String,Object> map = new HashMap<>();

    //创建读写锁对象
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    //放数据
    public void put(String key,Object value){
        //添加写锁
        rwLock.writeLock().lock();

        try {
            System.out.println(Thread.currentThread().getName()+" 正在写操作"+key);
            //暂停一会
            TimeUnit.MICROSECONDS.sleep(300);
            //放数据
            map.put(key,value);
            System.out.println(Thread.currentThread().getName() + "写完了" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放写锁
            rwLock.writeLock().unlock();
        }
    }

    //取数据
    public Object get(String key){
        //添加写锁
        rwLock.readLock().lock();
        Object result = null;

        try {
            System.out.println(Thread.currentThread().getName()+" 正在读操作"+key);
            //暂停一会
            TimeUnit.MICROSECONDS.sleep(300);
            //放数据
            result = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读完了" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放写锁
            rwLock.readLock().unlock();
        }
        return result;
    }
    
}    

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        
        MyCache myCache = new MyCache();
        
        for (int i = 1; i <= 5; i++) {
            int num = i;
            new Thread(()->{
                myCache.put(num+"",num+"");
            },String.valueOf(i)).start();
        }

        for (int i = 1; i <= 5; i++) {
            int num = i;
            new Thread(()->{
                myCache.get(num+"");
            },String.valueOf(i)).start();
        }
    }
}
