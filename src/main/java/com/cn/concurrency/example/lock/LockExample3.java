package com.cn.concurrency.example.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock
 *
 * 在没有任何读写锁的操作下，才能获取写锁。
 *
 * 悲观读取。
 *
 * 写写/读写 需要“互斥”
 * 读读 不需要互斥
 */
@Slf4j
public class LockExample3 {

    private final Map<String, Data> map = new TreeMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock readLock = lock.readLock();

    /**
     * 在没有任何读写锁的操作下，才能获取写锁。
     */
    private final Lock writeLock = lock.writeLock();

    //读数据
    public Data get(String key) {

        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    //读操作
    public Set<String> getAllKeys() {

        readLock.lock();
        try {
            return map.keySet();
        } finally {
            readLock.unlock();
        }
    }

    //写操作
    //如果读操作太频繁，读锁会出现在一直等待(锁饥饿)
    public Data put(String key, Data value) {

        writeLock.lock();
        try {
            return map.put(key, value);
        } finally {
            readLock.unlock();
        }
    }

    class Data {

    }
}
