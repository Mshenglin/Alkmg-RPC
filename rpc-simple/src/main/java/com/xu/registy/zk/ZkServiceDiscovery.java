package com.xu.registy.zk;

import com.xu.extension.ExtensionLoader;
import com.xu.loadbalance.LoadBalance;
import com.xu.registy.ServiceDiscovery;

import java.net.InetSocketAddress;

/**
 * 发现注册服务，使用负载均衡算法返回服务列表中的一个地址给客户端
 * @author mashenglin
 */
public class ZkServiceDiscovery implements ServiceDiscovery {
    /**
     * 加载用来做负载均衡的类
     */
    private final LoadBalance loadBalance;
    public ZkServiceDiscovery(){
        this.loadBalance= ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }
    @Override
    public InetSocketAddress lookupService(String rpcServiceName)
    {
        //获取Zookeeper连接

        return null;
    }
}
