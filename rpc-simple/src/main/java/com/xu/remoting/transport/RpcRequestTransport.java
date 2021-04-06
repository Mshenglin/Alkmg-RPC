package com.xu.remoting.transport;

import com.xu.extension.SPI;
import com.xu.remoting.dto.RpcRequest;

/**
 *发送 RpcRequest
 * @author mashenglin
 */
@SPI
public interface RpcRequestTransport {
    /**
     * 发送rpc请求给server和得到结果
     * @param rpcRequest 消息主体
     * @return  server返回的数据
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
