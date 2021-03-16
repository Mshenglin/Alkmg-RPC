package com.xu.proxy;

import com.xu.entity.RpcServiceProperties;
import com.xu.enums.RpcErrorMessageEnum;
import com.xu.enums.RpcResponseCodeEnum;
import com.xu.exception.RpcException;
import com.xu.remoting.dto.RpcRequest;
import com.xu.remoting.dto.RpcResponse;
import com.xu.remoting.transport.RpcRequestTransport;
import com.xu.remoting.transport.socket.SocketRpcClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 动态代理类
 * 当动态代理对象调用一个方法的时候，它实际上调用了下面的invoke方法。
 * 正是由于动态代理，客户端调用的远程方法就像调用本地方法一样(中间过程是屏蔽的)
 * @author mashenglin
 */
@Slf4j
public class RpcClientProxy  implements InvocationHandler  {
    private static final String INTERFACE_NAME = "interfaceName";
    /**
     * 用于向服务器发送请求。有两种实现:套接字和netty
     */
    private final RpcRequestTransport rpcRequestTransport;
    /**
     * 传输用的实体对象
     */
    private final RpcServiceProperties rpcServiceProperties;

    /**
     * 构造方法
     * @param rpcRequestTransport
     * @param rpcServiceProperties
     */
    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceProperties rpcServiceProperties) {
        this.rpcRequestTransport = rpcRequestTransport;
        if (rpcServiceProperties.getGroup() == null) {
            rpcServiceProperties.setGroup("");
        }
        if (rpcServiceProperties.getVersion() == null) {
            rpcServiceProperties.setVersion("");
        }
        this.rpcServiceProperties = rpcServiceProperties;
    }
    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceProperties = RpcServiceProperties.builder().group("").version("").build();
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ExecutionException, InterruptedException {
        log.info("invoked method: [{}]", method.getName());
        //rpc请求
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceProperties.getGroup())
                .version(rpcServiceProperties.getVersion())
                .build();
        //用于返回的响应
        RpcResponse<Object> rpcResponse = null;
//       //判断请求方式
//        if (rpcRequestTransport instanceof NettyRpcClient) {
//            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
//            rpcResponse = completableFuture.get();
//        }
        if (rpcRequestTransport instanceof SocketRpcClient) {
            rpcResponse = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        //检查是否合法
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }
    /**
     * 获取到代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
    /**
     * 检查请求和响应是否合法
     * @param rpcResponse
     * @param rpcRequest
     */
    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
