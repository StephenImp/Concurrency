package com.cn.concurrency.example.singleton;

import com.cn.concurrency.annoations.NotThreadSafe;

/**
 * 懒汉模式 -》 双重同步锁单例模式
 * 单例实例在第一次使用时进行创建
 */
@NotThreadSafe
public class SingletonExample4 {

    // 私有构造函数
    private SingletonExample4() {

    }


    /**
     * 多线程条件下，会发生的问题
     *      JVM和cpu优化，发生了指令重排
     */
    // 1、memory = allocate() 分配对象的内存空间
    // 3、instance = memory 设置instance指向刚分配的内存
    // 2、ctorInstance() 初始化对象

    // 单例对象
    private static SingletonExample4 instance = null;

    // 静态的工厂方法
    public static SingletonExample4 getInstance() {

        if (instance == null) { // 双重检测机制        // ②线程B执行到这一步,发现有值了直接就return了。就会出现问题。
            synchronized (SingletonExample4.class) { // 同步锁
                if (instance == null) {
                    instance = new SingletonExample4(); // ①线程A执行到这一步,刚刚初始化对象，还没有分配内存

                    // new 一个SingletonExample4()的时候，CPU执行的指令
                    // 1、memory = allocate() 分配对象的内存空间
                    // 2、ctorInstance() 初始化对象
                    // 3、instance = memory 设置instance指向刚分配的内存
                }
            }
        }
        return instance;
    }
}
