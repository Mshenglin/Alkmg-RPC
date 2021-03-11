package com.xu.registy.zk.util;

import org.apache.curator.framework.CuratorFramework;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zookeeper 节点操作的工具类
 * @author mashenglin
 */
public class CuratorUtils {
    /**
     * 最大重连时长
     */
    private static final int BASE_SLEEP_TIME = 1000;
    /**
     * 最多重连次数
     */
    private static final int MAX_RETRIES = 3;
    /**
     * 用于存储节点的路径
     */
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    /**
     * 用来存储节点的数据
     */
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    /**
     * 用来连接服务端的客户端对象
     */
    private static CuratorFramework zkClient;
    /**
     * 默认的服务端地址
     */
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    private CuratorUtils() {
    }
    /**
     * 创建持久的节点。与临时节点不同，当客户端断开连接时，持久化节点不会被删除
     */

}
