package com.xu.loadbalance.loadbalancer;

import com.xu.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * 随机算法
 * @author mashenglin
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    @Override
    protected String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
