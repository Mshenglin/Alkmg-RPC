package com.xu.netty.dto;

import lombok.*;

/**
 * 客户端请求实体类
 * @author Dell
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class RpcRequest {
    private String interfaceName;
    private String methodName;
}
