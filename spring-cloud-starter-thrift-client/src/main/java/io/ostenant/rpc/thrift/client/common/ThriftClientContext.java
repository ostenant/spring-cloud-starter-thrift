package io.ostenant.rpc.thrift.client.common;

import io.ostenant.rpc.thrift.client.pool.TransportKeyedObjectPool;
import io.ostenant.rpc.thrift.client.properties.ThriftClientProperties;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThriftClientContext {

    private static final Lock lock = new ReentrantLock();

    private static ThriftClientContext context;

    private ThriftClientContext() {
    }

    private ThriftClientProperties properties;

    private TransportKeyedObjectPool objectPool;

    private String registryAddress;

    public static ThriftClientContext context(ThriftClientProperties properties, TransportKeyedObjectPool objectPool) {
        context().properties = properties;
        context().objectPool = objectPool;
        return context;
    }

    public static ThriftClientContext context() {
        if (context == null) {
            try {
                lock.lock();
                if (context == null) {
                    context = new ThriftClientContext();
                }
            } finally {
                lock.unlock();
            }
        }
        return context;
    }

    public static void registry(String registryAddress) {
        context().registryAddress = registryAddress;
    }

    public ThriftClientProperties getProperties() {
        return context.properties;
    }

    public TransportKeyedObjectPool getObjectPool() {
        return context.objectPool;
    }

    public String getRegistryAddress() {
        return context.registryAddress;
    }
}
