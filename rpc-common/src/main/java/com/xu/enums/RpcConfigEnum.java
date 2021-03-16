package com.xu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Rpc配置枚举
 * @author mashenglin
 */
@AllArgsConstructor
@Getter
public enum  RpcConfigEnum {
    /**
     *
     */
    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
