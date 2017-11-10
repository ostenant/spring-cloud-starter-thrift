package com.icekredit.rpc.thrift.client.pool;

import com.icekredit.rpc.thrift.client.common.ThriftServerNode;
import com.icekredit.rpc.thrift.client.exception.ThriftClientConfigException;
import com.icekredit.rpc.thrift.client.exception.ThriftClientOpenException;
import com.icekredit.rpc.thrift.client.factory.ThriftTransportFactory;
import com.icekredit.rpc.thrift.client.properties.ThriftClientProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.PooledSoftReference;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.Objects;

public class TransportKeyedPooledObjectFactory extends BaseKeyedPooledObjectFactory<ThriftServerNode, TTransport> {

    private Logger log = LoggerFactory.getLogger(getClass());
    private ThriftClientProperties properties;

    public TransportKeyedPooledObjectFactory(ThriftClientProperties properties) {
        this.properties = properties;
    }


    @Override
    public TTransport create(ThriftServerNode key) throws Exception {
        if (StringUtils.isBlank(key.getHost())) {
            throw new ThriftClientConfigException("Invalid Thrift server, node IP address: " + key.getHost());
        }

        if (key.getPort() <= 0 || key.getPort() > 65535) {
            throw new ThriftClientConfigException("Invalid Thrift server, node port: " + key.getPort());
        }

        TTransport transport = ThriftTransportFactory.determineTTranport(properties.getServiceModel(), key);

        try {
            transport.open();
            log.info("Open a new transport {}", transport);
        } catch (TTransportException e) {
            throw new ThriftClientOpenException("Connect to " + key.getHost() + ":" + key.getPort() + " failed", e);
        }
        return transport;
    }


    @Override
    public PooledObject<TTransport> wrap(TTransport value) {
        SoftReference<TTransport> softReference = new SoftReference<>(value);
        return new PooledSoftReference<>(softReference);
    }


    @Override
    public boolean validateObject(ThriftServerNode key, PooledObject<TTransport> value) {
        PooledSoftReference<TTransport> softReference = (PooledSoftReference<TTransport>) value;
        TTransport transport = softReference.getObject();

        if (Objects.isNull(transport)) {
            log.warn("Pooled transport has been destroyed, information: {}" + softReference.toString());
            return false;
        }

        try {
            return transport.isOpen();
        } catch (Exception e) {
            log.error(e.getCause().getMessage());
            return false;
        }
    }


    @Override
    public void destroyObject(ThriftServerNode key, PooledObject<TTransport> value) throws Exception {
        PooledSoftReference<TTransport> softReference = (PooledSoftReference<TTransport>) value;
        TTransport transport = softReference.getObject();

        if (Objects.isNull(transport)) {
            log.warn("Pooled transport has been destroyed, information: {}" + softReference.toString());
            return;
        }

        if (transport.isOpen()) {
            log.info("Pooled transport is destroyed normally");
            transport.close();
        }

    }

}
