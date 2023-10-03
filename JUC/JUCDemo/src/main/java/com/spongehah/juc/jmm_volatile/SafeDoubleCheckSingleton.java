package com.spongehah.juc.jmm_volatile;

/**
 * 单例模式双端锁需要使用volatile
 */
public class SafeDoubleCheckSingleton {
    //通过volatile声明，实现线程安全的延迟初始化。
    private volatile static SafeDoubleCheckSingleton singleton;
    //私有化构造方法
    private SafeDoubleCheckSingleton(){}
    /**
     * 其中第3步中实例化Singleton分多步执行（分配内存空间、初始化对象、将对象指向分配
     * 的内存空间)，某些编译器为了性能原因，会将第二步和第三步进行重排序（分配内存空
     * 间、将对象指向分配的内存空间、初始化对象)。这样，某个线程可能会获得一个未完全
     * 初始化的实例：
     */
    //双重锁设计
    public static SafeDoubleCheckSingleton getInstance(){
        if (singleton == null){
            //1.多线程并发剑建对象时，会通过加锁保证只有一个线程能创建对象
            synchronized (SafeDoubleCheckSingleton.class){
                if (singleton == null){
                    //隐患：多线程环境下，由于重排序，该对象可能还未完成初始化就被其他线程读取
                    //解决隐患原理：利用volatile，禁止"初始化对象"和"设置singleton:指向内存空间"的重排序
                    singleton = new SafeDoubleCheckSingleton();
                }
            }
        }
        //2.对象刻建完毕，执行getInstance()将不需要茨取锁，直接返回创建对象
        return singleton;
    }
}
