package com.xu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 响应状态码枚举类
 * @author Dell
 */
@AllArgsConstructor
@Getter
@ToString
public enum  RpcResponseCodeEnum {



    /**
     * 响应失败
     */
    FAIL(500,"The remote call is fail"),
    /**
     * 响应成功
     */
    SUCCESS(200,"The remote call is successful");

    private final int code;
    private  final String message;

}
