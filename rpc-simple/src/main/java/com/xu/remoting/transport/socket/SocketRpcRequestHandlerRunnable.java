package com.xu.remoting.transport.socket;

import com.xu.factory.SingletonFactory;
import com.xu.remoting.dto.RpcRequest;
import com.xu.remoting.dto.RpcResponse;
import com.xu.remoting.handler.RpcRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author mashenglin
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable{
    /**
     * 客户端的请求
     */
    private final Socket socket;
    /**
     * 用于处理客户端的类
     */
    private final RpcRequestHandler rpcRequestHandler;
    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }
    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try ( //获取输入输出流
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            //得到客户端的请求
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            //将该请求交给RpcRequest 处理器进行处理返回
            Object result = rpcRequestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
    }
}
