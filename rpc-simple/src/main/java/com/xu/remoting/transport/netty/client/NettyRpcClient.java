package com.xu.remoting.transport.netty.client;

import com.xu.enums.CompressTypeEnum;
import com.xu.enums.SerializationTypeEnum;
import com.xu.extension.ExtensionLoader;
import com.xu.factory.SingletonFactory;
import com.xu.registry.ServiceDiscovery;
import com.xu.remoting.constants.RpcConstants;
import com.xu.remoting.dto.RpcMessage;
import com.xu.remoting.dto.RpcRequest;
import com.xu.remoting.dto.RpcResponse;
import com.xu.remoting.transport.RpcRequestTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * netty实现的客户端
 * @author mashenglin
 */
@Slf4j
public class NettyRpcClient implements RpcRequestTransport {
    /**
     * 服务发现
     */
    private final ServiceDiscovery serviceDiscovery;
    /**
     * 存放未被服务器请求的类
     */
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    /**
     * 客户端启动类
     */
    private final Bootstrap bootstrap;
    /**
     * 用于管理调度
     */
    private final EventLoopGroup eventLoopGroup;
    public NettyRpcClient() {
        // 初始化资源 EventLoopGroup, Bootstrap
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //IO类型
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                //  如果 15 秒之内没有发送数据给服务端的话，就发送一次心跳请求
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        //如果15秒内没有数据发送到服务器，则会触发心跳请求
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 向服务端发送消息
     * @param rpcRequest 消息主体
     * @return
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 创建返回用的元素
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        //获取请求的名称
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        // 更据请求的名称去注册中心获取到服务器的地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        // 得到与服务端的连接通道
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // 如果通道还在，就将Rpc的请求放入容器中
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);

            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setData(rpcRequest);
            rpcMessage.setCodec(SerializationTypeEnum.KYRO.getCode());
            rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
            rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    /**
     * 连接服务器并获取通道，以便可以向服务器发送rpc消息
     * @param inetSocketAddress 服务端地址
     * @return 连接服务器并获取通道，以便您可以向服务器发送rpc消息
     */
    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture=new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future->{
            if (future.isSuccess()){
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
              } else {
            throw new IllegalStateException();
        }
    });
        return completableFuture.get();
        }
    @SneakyThrows
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
    }
