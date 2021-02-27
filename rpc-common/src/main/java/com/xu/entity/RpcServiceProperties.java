package com.xu.entity;

import lombok.*;

/**
 *
 *
 * @author mashenglin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceProperties {
    /**
     * service版本
     */
    private String version;
    /**
     *当接口有多个实现类时，将他们按组分好
     */
    private String group;
    private String serviceName;
    public String toRpcServiceName(){
        return this.getServiceName();
    }
}
