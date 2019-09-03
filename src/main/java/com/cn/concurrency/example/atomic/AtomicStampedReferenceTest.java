package com.cn.concurrency.example.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {
    private static AtomicStampedReference<Integer> atomic = new AtomicStampedReference<>(100,0);

    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                //和乐观相似,第一个stamp相当于 version 每次加1 如果别的线程修改过就是1了.第一个线程0就对比不成功
                boolean sucess =   atomic.compareAndSet(100,101,atomic.getStamp(),atomic.getStamp()+1);
                System.out.println(sucess);
                sucess =   atomic.compareAndSet(101,100,atomic.getStamp(),atomic.getStamp()+1);
                System.out.println(sucess);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(()->{
            try {
                int stamp = atomic.getStamp();
                System.out.println("before:   "+stamp);//这里获取到的stamp是1
                TimeUnit.SECONDS.sleep(2);
                int stamp1 = atomic.getStamp();
                System.out.println("第一个线程的 stamp "+ stamp1);//这里第一个线程的stamp已经变成2 了
                //和乐观相似,第一个stamp相当于 version 每次加1 如果别的线程修改过就是1了.第一个线程0就对比不成功
                boolean b = atomic.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println("2线程= "+b);
                atomic.compareAndSet(101,100,stamp,stamp+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
