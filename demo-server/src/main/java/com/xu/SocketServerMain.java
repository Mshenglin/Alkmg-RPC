package com.xu;

import com.xu.remoting.transport.socket.SocketRpcServer;
import com.xu.serviceImpl.HelloServiceImpl;

/**
 * 使用Socket进行服务的注册
 * @author mashenglin
 */
public class SocketServerMain {
    public static void main(String[] args) {
        //创建服务者
        HelloService helloService = new HelloServiceImpl();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
    }
}
