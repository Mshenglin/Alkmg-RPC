package com.xu.remoting.transport.socket;

import com.xu.entity.RpcServiceProperties;
import com.xu.extension.ExtensionLoader;
import com.xu.registry.ServiceDiscovery;
import com.xu.remoting.dto.RpcRequest;
import com.xu.remoting.transport.RpcRequestTransport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于Socket传输RpcRequest
 * @author mashenglin
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RpcRequestTransport {
    /**
     * 服务发现,使用Dubbo中的spi机制
     */
    private final ServiceDiscovery serviceDiscovery;
    public SocketRpcClient(){
        this.serviceDiscovery= ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        //创建一个RpcName通过rpcRequest
        String rpcServiceName = RpcServiceProperties.builder().serviceName(rpcRequest.getInterfaceName())
                .group(rpcRequest.getGroup()).version(rpcRequest.getVersion()).build().toRpcServiceName();
        //根据rpcServiceName名称获取到地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        //通过Socket向服务端发送请求
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Read RpcResponse from the input stream
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
