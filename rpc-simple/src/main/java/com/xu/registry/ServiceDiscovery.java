package com.xu.registry;

import com.xu.extension.SPI;

import java.net.InetSocketAddress;

/**
 * 服务的发现
 * @author mashenglin
 */
@SPI
public interface ServiceDiscovery {
    /**
     * 通过 rpcServiceName 寻找service
     * @param rpcServiceName rpc 服务的名称
     * @return service address
     */
    InetSocketAddress lookupService(String rpcServiceName);
}
