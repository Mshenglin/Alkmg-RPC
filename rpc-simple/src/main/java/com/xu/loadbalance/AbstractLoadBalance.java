package com.xu.loadbalance;

import java.util.List;

/**
 *配置负载均衡
 * @author mashenglin
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
        //特殊值的判断
        if(serviceAddresses==null||serviceAddresses.size()==0){
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcServiceName);
    }

    /**
     * 用来选择service服务
     * @param serviceAddresses service地址列表
     * @param rpcServiceName 需要调用的service名称
     * @return 选取出的service地址
     */
    protected abstract String doSelect(List<String> serviceAddresses, String rpcServiceName);
}
