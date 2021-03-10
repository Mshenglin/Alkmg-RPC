package com.xu.provider;

import com.xu.entity.RpcServiceProperties;
import com.xu.enums.RpcErrorMessageEnum;
import com.xu.exception.RpcException;
import com.xu.extension.ExtensionLoader;
import com.xu.registy.ServiceRegistry;
import com.xu.remoting.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class ServiceProviderImpl implements ServiceProvider{
    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }
    @Override
    public void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        //得到rpcServiceName
        String rpcServiceName = rpcServiceProperties.toRpcServiceName();
        //如果在注册中心已经存在，那么就直接返回
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        //如果不存在就向注册中心添加该服务
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, service);
        log.info("Add service: {} and interfaces:{}", rpcServiceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        //在map中根据服务名称获取到该服务的对象
        Object service = serviceMap.get(rpcServiceProperties.toRpcServiceName());
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(Object service) {
        this.publishService(service, RpcServiceProperties.builder().group("").version("").build());
    }
    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            //获取本机的IP地址
            String host = InetAddress.getLocalHost().getHostAddress();
            //获取服务类实现的第一个接口
            Class<?> serviceRelatedInterface = service.getClass().getInterfaces()[0];
            //获取该类的名称
            String serviceName = serviceRelatedInterface.getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            this.addService(service, serviceRelatedInterface, rpcServiceProperties);
            //将该服务注册
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(), new InetSocketAddress(host, NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
