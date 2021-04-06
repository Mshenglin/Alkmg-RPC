package com.xu.registry.zk;

import com.xu.enums.RpcErrorMessageEnum;
import com.xu.exception.RpcException;
import com.xu.extension.ExtensionLoader;
import com.xu.extension.SPI;
import com.xu.loadbalance.LoadBalance;
import com.xu.registry.ServiceDiscovery;
import com.xu.registry.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 发现注册服务，使用负载均衡算法返回服务列表中的一个地址给客户端
 * @author mashenglin
 */
@Slf4j
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
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        //根据rpcServiceName获取到该服务的所有地址
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        //使用负载均衡算法，选取其中的一个地址返回
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcServiceName);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
