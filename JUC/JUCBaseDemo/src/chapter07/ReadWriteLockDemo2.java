package chapter07;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁降级：写锁 -》 读锁 -》 释放写锁 -》 释放读锁
 * 只能由写锁降级为读锁
 * 但是不能由读锁升级为写锁
 * 因为读锁是共享的，你读的时候可能还有其他人正在读
 * 而写锁是独占的，当前只有你一个人在写，所以可以降级为读锁
 */
public class ReadWriteLockDemo2 {
    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        
        writeLock.lock();

        System.out.println("write");
        
        readLock.lock();

        System.out.println("read");

        writeLock.unlock();

        readLock.unlock();
    }
}
