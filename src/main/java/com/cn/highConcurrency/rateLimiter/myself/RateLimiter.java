package com.cn.highConcurrency.rateLimiter.myself;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

/**
 * Created by MOZi on 2019/1/29.
 *
 * 限流器
 *
 * 核心代码在acquire方法部分主要思路就是，
 *
 *      优先采用CAS进行自旋操作获取令牌，一直尝试令牌消耗光，那么会进入等待，
 *      在定时线程调用supplement方法时，会唤醒所有等待线程，
 *      此时进入CAS进行尝试消耗令牌，以此循环一直到设置的最大等待时间（代码中的expect）消耗光，
 *      如果还没获得令牌，那么会返回false
 */
public class RateLimiter {

    private volatile int token;
    private final int originToken;

    private static Unsafe unsafe = null;
    private static final long valueOffset;
    private final Object lock = new Object();

    static {
        try {
            // 应用开发中使用unsafe对象必须通过反射获取
            Class<?> clazz = Unsafe.class;
            Field f = clazz.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(clazz);
            valueOffset = unsafe.objectFieldOffset(RateLimiter.class.getDeclaredField("token"));
        } catch (Exception ex) {throw new Error(ex);}
    }

    public RateLimiter(int token){
        this.originToken = token;
        this.token = token;
    }

    /**
     * 获取一个令牌
     */
    public boolean acquire(){

        int current = token;
        if(current<=0){
            // 保证在token已经用光的情况下依然有获取竞争的能力
            current = originToken;
        }

        long expect = 1000;// max wait 1s
        long future = System.currentTimeMillis()+expect;
        while(current>0){
            if(compareAndSet(current, current-1)){
                return true;
            }
            current = token;
            if(current<=0 && expect>0){
                // 在有效期内等待通知
                synchronized (lock){
                    try {
                        lock.wait(expect);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                current = token;
                if(current<=0){
                    current = originToken;
                }
                expect = future - System.currentTimeMillis();
            }
        }
        return false;
    }

    private boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    /**
     * 补充令牌
     */
    public void supplement(final ExecutorService executorService){
        this.token = originToken;
        executorService.execute(() -> {
            synchronized (lock){
                lock.notifyAll();
            }
        });
    }
}
