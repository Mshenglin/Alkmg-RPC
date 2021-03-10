package com.xu.registy.zk;

import com.xu.registy.ServiceDiscovery;

import java.net.InetSocketAddress;

/**
 * @author mashenglin
 */
public class ZkServiceDiscovery implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        return null;
    }
}
