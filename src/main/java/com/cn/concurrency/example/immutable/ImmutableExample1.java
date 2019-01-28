package com.cn.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.cn.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * 定义不可变对象 方式①
 *
 *  final
 */
@Slf4j
@NotThreadSafe
public class ImmutableExample1 {

    private final static Integer a = 1;
    private final static String b = "2";

    //com.google.common.collect.Maps;
    private final static Map<Integer, Integer> map = Maps.newHashMap();

    static {
        map.put(1, 2);
        map.put(3, 4);
        map.put(5, 6);
    }

    public static void main(String[] args) {
//        a = 2;
//        b = "3";
//        map = Maps.newHashMap();  这里指向新的对象是不允许的

        /**
         * 值是允许修改的,就是说被final修饰的对象，它的引用是不能变的
         */
        map.put(1, 3);
        log.info("{}", map.get(1));
    }

    private void test(final int a) {
//        a = 1;
    }
}
