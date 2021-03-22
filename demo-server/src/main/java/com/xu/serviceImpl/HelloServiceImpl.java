package com.xu.serviceImpl;

import com.xu.Hello;
import com.xu.HelloService;
import com.xu.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mashenglin
 */
@Slf4j
@RpcService(group = "test1", version = "version1")
public class HelloServiceImpl implements HelloService {
    static {
        System.out.println("HelloServiceImpl被创建");
    }
    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
