package com.xu.loadbalance.ZkServiceRegistryTest;


import com.xu.registry.ServiceDiscovery;
import com.xu.registry.ServiceRegistry;
import com.xu.registry.zk.ZkServiceDiscovery;
import com.xu.registry.zk.ZkServiceRegistry;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author shuang.kou
 * @createTime 2020年05月31日 16:25:00
 */
class ZkServiceRegistryTest {

    @Test
    void should_register_service_successful_and_lookup_service_by_service_name() {
        ServiceRegistry zkServiceRegistry = new ZkServiceRegistry();
        InetSocketAddress givenInetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);
        zkServiceRegistry.registerService("com.xu.registry.zk.ZkServiceRegistry", givenInetSocketAddress);
        ServiceDiscovery zkServiceDiscovery = new ZkServiceDiscovery();
        InetSocketAddress acquiredInetSocketAddress = zkServiceDiscovery.lookupService("com.xu.registry.zk.ZkServiceRegistry");
        assertEquals(givenInetSocketAddress.toString(), acquiredInetSocketAddress.toString());
    }
}
