package com.xu.remoting.transport.socket;

import com.xu.config.CustomShutdownHook;
import com.xu.entity.RpcServiceProperties;
import com.xu.factory.SingletonFactory;
import com.xu.provider.ServiceProvider;
import com.xu.provider.ServiceProviderImpl;
import com.xu.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static com.xu.remoting.transport.netty.server.NettyRpcServer.PORT;

/**
 * Socket服务端对客户端进行响应
 * @author mashenglin
 */
@Slf4j
public class SocketRpcServer {
    /**
     * 使用线程池去处理客户端的请求
     */
    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;
    public SocketRpcServer(){
        threadPool= ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider= SingletonFactory.getInstance(ServiceProviderImpl.class);
    }
    public void registerService(Object service) {
        serviceProvider.publishService(service);
    }

    public void registerService(Object service, RpcServiceProperties rpcServiceProperties) {
        serviceProvider.publishService(service, rpcServiceProperties);
    }
    public void start() {
        //创建服务端
        try (ServerSocket server = new ServerSocket()) {
            //得到本机的ip地址
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }
}
