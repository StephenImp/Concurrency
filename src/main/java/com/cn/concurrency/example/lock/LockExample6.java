package com.cn.concurrency.example.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition
 *
 * 通过控制AQS队列与Condition队列，实现线程间顺序通信
 */
@Slf4j
public class LockExample6 {

    public static void main(String[] args) {

        ReentrantLock reentrantLock = new ReentrantLock();

        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            try {
                reentrantLock.lock();//加入到AQS队列中。--加锁
                log.info("wait signal"); // 1       线程①输出等待信号
                condition.await();  //从AQS队列中移除--锁的释放       加入到Condition的等待队列中去
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("get signal"); // 4    这时，AQS按照重头到尾的顺序唤醒线程，这时AQS队列中，只剩线程①的锁，线程①被唤醒了，线程①继续执行
            reentrantLock.unlock();//线程①继续执行
        }).start();

        new Thread(() -> {
            reentrantLock.lock();//线程①释放锁后，被唤醒  获取锁 加入到AQS队列中
            log.info("get lock"); // 2
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signalAll();//发送信号
            log.info("send signal ~ "); // 3
            reentrantLock.unlock();//线程②解锁
        }).start();
    }
}
