①synchronized    当只有少量竞争者的时候呢，synchronized是很好的通用锁实现。
                    JVM会帮我们解锁，不会出现死锁

②竞争者不少，竞争者增长趋势是可以预估的，ReentrantLock是不错的选择
                选择其他的锁，有可能会产生死锁。