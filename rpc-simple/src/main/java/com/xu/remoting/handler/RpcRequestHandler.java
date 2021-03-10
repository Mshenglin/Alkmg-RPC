package com.xu.remoting.handler;

import com.xu.exception.RpcException;
import com.xu.factory.SingletonFactory;
import com.xu.provider.ServiceProvider;
import com.xu.provider.ServiceProviderImpl;
import com.xu.registy.ServiceRegistry;
import com.xu.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequest 处理器
 * @author mashenglin
 */
@Slf4j
public class RpcRequestHandler {
        private final ServiceProvider serviceProvider;
        public  RpcRequestHandler() {
            serviceProvider= SingletonFactory.getInstance(ServiceProviderImpl.class);
        }
/**
 * 处理rpcRequest:调用相应的方法，然后返回该方法
 */
public Object handle(RpcRequest rpcRequest) {
    Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
    return invokeTargetMethod(rpcRequest, service);
}
    /**
     * 获取方法执行结果
     *
     * @param rpcRequest 客户端的请求
     * @param service  服务的对象
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        //用于结果的返回
        Object result;
        try {
            //通过反射获取到请求的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            //执行方法得到结果
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        //将结果返回
        return result;
    }
}
