package com.cn.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * synchronized  不属于方法声明的一部分
 *      所以子类是无法继承父类方法所修饰的synchronized
 */
@Slf4j
public class SynchronizedExample1 {

    // 修饰一个代码块
    public void test1(int j) {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                log.info("test1 {} - {}", j, i);
            }
        }
    }

    // 修饰一个方法
    public synchronized void test2(int j) {
        for (int i = 0; i < 10; i++) {
            log.info("test2 {} - {}", j, i);
        }
    }

    public static void main(String[] args) {

        SynchronizedExample1 example1 = new SynchronizedExample1();
        SynchronizedExample1 example2 = new SynchronizedExample1();

        ExecutorService executorService = Executors.newCachedThreadPool();

        /**
         * 因为是两个线程池
         *
         * 两个不同的对象去调用的话，是无法锁住的。
         */

        executorService.execute(() -> {
            example1.test2(1);
        });

        executorService.execute(() -> {
            example2.test2(2);
        });
    }
}
