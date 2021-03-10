package com.xu.provider;

import com.xu.entity.RpcServiceProperties;

/**
 * 存储和提供服务对象。
 * @author mashenglin
 */
public interface ServiceProvider {
    /**
     * 添加服务
     * @param service              服务的对象
     * @param serviceClass         由服务实例对象实现的接口类
     * @param rpcServiceProperties 服务的相关属性
     */
    void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties);

    /**
     * 得到服务
     * @param rpcServiceProperties 服务的相关属性
     * @return 服务的对象
     */
    Object getService(RpcServiceProperties rpcServiceProperties);

    /**
     * 发布服务
     * @param service              服务的对象
     * @param rpcServiceProperties 服务的相关属性
     */
    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * 发布服务
     * @param service 服务的对象
     */
    void publishService(Object service);
}
