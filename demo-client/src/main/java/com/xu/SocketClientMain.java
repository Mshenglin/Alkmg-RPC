package com.xu;

import com.xu.entity.RpcServiceProperties;
import com.xu.proxy.RpcClientProxy;
import com.xu.remoting.transport.RpcRequestTransport;
import com.xu.remoting.transport.socket.SocketRpcClient;

/**
 * @author mashenglin
 */
public class SocketClientMain {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport=new SocketRpcClient();
        RpcServiceProperties rpcServiceProperties=RpcServiceProperties.builder().group("test2").version("version2").build();
        RpcClientProxy rpcClientProxy=new RpcClientProxy(rpcRequestTransport, rpcServiceProperties);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello=helloService.hello(new Hello("111","222"));
        System.out.println(hello);
    }
}
