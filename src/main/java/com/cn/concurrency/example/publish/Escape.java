package com.cn.concurrency.example.publish;

import com.cn.concurrency.annoations.NotRecommend;
import com.cn.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象溢出
 *
 * Escape对象在没有完全构造完成之前就使用 thisCanBeEscape 属性
 * 这样是不安全的
 *
 *  this 引用会溢出
 *
 *  如果要在构造函数中，创建线程
 *  那么，采用专有的start 或初始化的方法，来统一启动线程
 *
 *  工厂方法和私有构造函数，来完成对象创建和监听器的注册等等
 *  这样可以避免不正确的创建
 *
 *  目的：在对象未完成构造之前，不可以将其发布
 */
@Slf4j
@NotThreadSafe
@NotRecommend//不推荐
public class Escape {

    private int thisCanBeEscape = 0;

    public Escape () {
        new InnerClass();
    }

    private class InnerClass {

        public InnerClass() {
            log.info("{}", Escape.this.thisCanBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
