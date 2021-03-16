package com.xu.annotation;
/**
 * RPC引用注释，自动连接服务实现类
 *
 * @author mashenglin
 */
public @interface RpcReference {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

}
