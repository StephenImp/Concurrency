package com.cn.concurrency.example.threadLocal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/threadLocal")
public class ThreadLocalController {


    /**
     * 每一个请求，对于服务器来说，都是一个线程在运行
     *
     *      threadLocal  实现线程间的隔离。  相当于  一个线程中的全局变量吧。
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public Long test() {
        return RequestHolder.getId();
    }
}
