package com.icekredit.rpc.thrift.client.common;

import com.icekredit.rpc.thrift.client.pool.TransportKeyedObjectPool;
import com.icekredit.rpc.thrift.client.properties.ThriftClientProperties;

public class ThriftClientContext {

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
            synchronized (ThriftClientContext.class) {
                if (context == null) {
                    context = new ThriftClientContext();
                }
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
