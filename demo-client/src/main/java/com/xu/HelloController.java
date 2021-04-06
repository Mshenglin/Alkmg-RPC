package com.xu;

import com.xu.annotation.RpcReference;

/**
 * @author mashenglin
 */
public class HelloController {
    @RpcReference(version = "version1",group = "test1")
    private HelloService helloService;
public void  test() throws InterruptedException {
    String hello = this.helloService.hello(new Hello("111", "222"));
    Thread.sleep(12000);
    for (int i = 0; i < 10; i++) {
        System.out.println(helloService.hello(new Hello("111", "222")));
    }
}

}
