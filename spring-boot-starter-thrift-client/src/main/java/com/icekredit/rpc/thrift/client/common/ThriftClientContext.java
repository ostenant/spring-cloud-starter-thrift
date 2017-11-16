package com.icekredit.rpc.thrift.client.common;

import com.icekredit.rpc.thrift.client.pool.TransportKeyedObjectPool;
import com.icekredit.rpc.thrift.client.properties.ThriftClientProperties;

public class ThriftClientContext {

    private static ThriftClientContext context;

    private ThriftClientContext() {
    }

    private ThriftClientProperties properties;

    private TransportKeyedObjectPool objectPool;

    public static ThriftClientContext context(ThriftClientProperties properties, TransportKeyedObjectPool objectPool) {
        if (context == null) {
            synchronized (context) {
                if (context == null) {
                    context = new ThriftClientContext();
                    context.properties = properties;
                    context.objectPool = objectPool;
                }
            }
        }
        return context;
    }

    public ThriftClientProperties getProperties() {
        return properties;
    }

    public TransportKeyedObjectPool getObjectPool() {
        return objectPool;
    }

    public static ThriftClientContext getContext() {
        return context;
    }
}
