package com.xu.loadbalance;

import java.util.List;

/**
 * 配置负载均衡
 * @author mashenglin
 */
public interface LoadBalance {
    /**
     * 从存有以知service地址的List中选择一个返回
     * @param serviceAddresses service地址列表
     * @param rpcServiceName 需要调用的service名称
     * @return 选取出的service地址
     */
    String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName);
}
