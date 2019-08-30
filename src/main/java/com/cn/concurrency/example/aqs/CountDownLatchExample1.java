package com.cn.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * CountDownLatch ：闭锁，在完成某些运算时，只有其他所有线程的运算全部完成，当前运算才继续执行
 *
 * 主线程执行到闭锁等待时，主线程等待，当持有闭锁的副线程释放闭锁时，主线程才能继续执行
 */
@Slf4j
public class CountDownLatchExample1 {

    /**
     * 每次操作一个线程，
     *  new CountDownLatch(threadCount)
     *  200每开一个线程都会递减
     *  当变量变为0时，主线程操作就可以继续执行了
     */
    private final static int threadCount = 200;

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newCachedThreadPool();

        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    test(threadNum);
                } catch (Exception e) {
                    log.error("exception", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        log.info("@@@@@@@@@@finish@@@@@@@@@@@@@@");
        exec.shutdown();
    }

    private static void test(int threadNum) throws Exception {
        Thread.sleep(100);
        log.info("{}", "***"+threadNum);
        Thread.sleep(100);
    }
}
