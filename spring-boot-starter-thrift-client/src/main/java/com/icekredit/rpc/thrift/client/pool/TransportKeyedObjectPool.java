package com.icekredit.rpc.thrift.client.pool;

import com.icekredit.rpc.thrift.client.common.ThriftServerNode;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.transport.TTransport;

public class TransportKeyedObjectPool extends GenericKeyedObjectPool<ThriftServerNode, TTransport> {

    public TransportKeyedObjectPool(KeyedPooledObjectFactory factory) {
        super(factory);
    }

    public TransportKeyedObjectPool(KeyedPooledObjectFactory factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }
}
