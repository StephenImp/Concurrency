package com.cn.highConcurrency.rateLimiter.myself;

import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by MOZi on 2019/1/29.
 *
 * 限流管理器
 *
 * 代码中主要是创建了定时线程，补充令牌线程池。
 *
 * 其中的不足
         在集群的环境下，没有考虑分布式的情况，也就是如果一个应用部署的限流是1s产生10个令牌，假设部署了5个应用，
         那么实际1s可以产生50个令牌。
         如果需要考虑这部分，那么在CAS操作可以替换为通过redis的setnx来进行获取锁操作然后更新redis存储对应的令牌，
         补充则直接设置更新redis对应的令牌数即可，这样效率肯定比现在基于CAS操作低。
 */
public class ConfineManager {

    // 定时线程
    private final ScheduledThreadPoolExecutor scheduledCheck = new ScheduledThreadPoolExecutor(2);
    // 执行补充线程池
//    private final ExecutorService executorService = new ThreadPoolExecutor(5, 200,
//            60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
//            new NamedThreadFactory("supplement",true,false));

    private final ExecutorService executorService = new ThreadPoolExecutor(5, 200,
            60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    // 限流器容器
    private Map<String,RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        scheduledCheck.scheduleAtFixedRate(new SupplementRateLimiter(), 1, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy(){
        scheduledCheck.shutdown();
    }

    /**
     * 通过key获取相应的限流器
     */
    public void acquire(String key,int tokenCount) throws Exception {

        RateLimiter rateLimiter = rateLimiterMap.get(key);
        // 双检锁确保安全创建
        if(rateLimiter==null){
            synchronized (this){
                // init RateLimiter
                rateLimiter = rateLimiterMap.computeIfAbsent(key, k -> new RateLimiter(tokenCount));
            }
        }
        // 尝试获取令牌
        if(!rateLimiter.acquire()){
            // 获取失败，根据实际情况进行处理，这里直接抛异常了
            throw new Exception("***");
        }
    }

    /**
     * 补充相应的令牌数
     */
    private class SupplementRateLimiter implements Runnable{
        @Override
        public void run(){
            rateLimiterMap.values().forEach(rateLimiter -> rateLimiter.supplement(executorService));
        }
    }
}
