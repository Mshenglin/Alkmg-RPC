package com.xu.remoting.transport.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于存放Channel(Channel用于服务端和客户端处理数据)
 * @author mashenglin
 */
@Slf4j
public class ChannelProvider {
    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param inetSocketAddress 服务端地址
     * @param channel
     */
    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        channelMap.put(key, channel);
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map size :[{}]", channelMap.size());
    }

    /**
     *
     * @param inetSocketAddress 服务端的地址
     * @return 服务端的请求
     */
    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // 确定是否有对应地址的连接
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            // 如果是，确定连接是否可用，如果是，直接获取它
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        return null;
    }
}
