package com.xu.remoting.dto;

import com.xu.entity.RpcServiceProperties;
import lombok.*;

import java.io.Serializable;

/**
 * rpc请求
 * @author mashenglin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    /**
     * 请求的id
     */
    private String requestId;
    /**
     * 接口的名称
     */
    private String interfaceName;
    /**
     * 方法的名称
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] parameters;
    /**
     * 参数的类型
     */
    private Class<?>[] paramTypes;
    /**
     * 版本
     */
    private String version;
    /**
     *所属组
     */
    private String group;
 public RpcServiceProperties toRpcProperties(){
     return RpcServiceProperties.builder().serviceName(this.getInterfaceName()).version(this.getVersion()).group(this.getGroup()).build();
 }
}
