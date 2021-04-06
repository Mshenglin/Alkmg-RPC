package com.xu.registry;

import com.xu.extension.SPI;

import java.net.InetSocketAddress;

/**
 * 服务的注册
 * @author mashenglin
 */
@SPI
public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
