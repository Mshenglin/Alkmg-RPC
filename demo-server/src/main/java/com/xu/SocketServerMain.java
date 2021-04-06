package com.xu;

import com.xu.entity.RpcServiceProperties;
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
        RpcServiceProperties rpcServiceProperties=RpcServiceProperties.builder().group("test2").version("version2").build();
        //向注册中心注册服务
        socketRpcServer.registerService(helloService, rpcServiceProperties);
        socketRpcServer.start();
    }
}
