package com.cn.concurrency;

import com.cn.concurrency.annoations.NotThreadSafe;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * Semaphore  与 CountDownLatch
 */
@Slf4j
@NotThreadSafe
public class ConcurrencyTest {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    public static int count = 0;

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newCachedThreadPool();

        //信号量
        final Semaphore semaphore = new Semaphore(threadTotal);

        //闭锁
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);


        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {

                    // 请求总数
                    semaphore.acquire();
                    add();
                    semaphore.release();

                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();//保证 clientTotal 减为0
        executorService.shutdown();
        log.info("count:{}", count);
    }


    private static void add() {
        count++;
    }
}
