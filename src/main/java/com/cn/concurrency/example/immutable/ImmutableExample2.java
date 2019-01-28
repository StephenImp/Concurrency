package com.cn.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.cn.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;


/**
 * 定义不可变对象 方式②
 *
 *  unmodifiableMap
 */
@Slf4j
@ThreadSafe
public class ImmutableExample2 {

    private static Map<Integer, Integer> map = Maps.newHashMap();

    static {
        map.put(1, 2);
        map.put(3, 4);
        map.put(5, 6);
        map = Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {

        //这里会有异常，这里的值是不允许修改的
        map.put(1, 3);
        log.info("{}", map.get(1));

    }

}
