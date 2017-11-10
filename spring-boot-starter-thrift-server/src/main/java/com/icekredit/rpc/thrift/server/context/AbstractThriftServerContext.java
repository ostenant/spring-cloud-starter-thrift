package com.icekredit.rpc.thrift.server.context;

import com.icekredit.rpc.thrift.server.properties.TServiceModel;
import com.icekredit.rpc.thrift.server.properties.ThriftServerProperties;
import com.icekredit.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractThriftServerContext {

    protected ThriftServerProperties properties;
    protected List<ThriftServiceWrapper> serviceWrappers;

    private volatile TServer thriftServer;

    private final Lock thriftServerLock = new ReentrantLock();

    public TServer buildServer() throws TTransportException, IOException {
        if (Objects.isNull(thriftServer)) {
            thriftServerLock.lock();
            try {
                return Objects.isNull(thriftServer) ? build() : thriftServer;
            } finally {
                thriftServerLock.unlock();
            }
        }
        return thriftServer;
    }

    private TServer build() throws TTransportException, IOException {
        String serviceModel = properties.getServiceModel();
        TServer tServer = null;

        if (StringUtils.equals(TServiceModel.SERVICE_MODEL_SIMPLE, serviceModel)) {
            tServer = buildTSimpleServer();
        }

        if (StringUtils.equals(TServiceModel.SERVICE_MODEL_NON_BLOCKING, serviceModel)) {
            tServer = buildTNonBlockingServer();
        }

        if (StringUtils.equals(TServiceModel.SERVICE_MODEL_THREAD_POOL, serviceModel)) {
            tServer = buildTThreadPoolServer();
        }

        if (StringUtils.equals(TServiceModel.SERVICE_MODEL_HS_HA, serviceModel)) {
            tServer = buildTHsHaServer();
        }

        if (StringUtils.equals(TServiceModel.SERVICE_MODEL_THREADED_SELECTOR, serviceModel)) {
            tServer = buildTThreadedSelectorServer();
        }

        thriftServer = Objects.isNull(tServer) ? buildDefaultTServer() : tServer;
        return getThriftServer();
    }

    TServer getThriftServer() {
        return thriftServer;
    }

    protected abstract TServer buildTNonBlockingServer() throws TTransportException, IOException;

    protected abstract TServer buildTSimpleServer() throws TTransportException, IOException;

    protected abstract TServer buildTThreadPoolServer() throws TTransportException, IOException;

    protected abstract TServer buildTHsHaServer() throws TTransportException, IOException;

    protected abstract TServer buildTThreadedSelectorServer() throws TTransportException, IOException;

    TServer buildDefaultTServer() throws TTransportException, IOException {
        return buildTHsHaServer();
    }

    protected ThriftServerProperties getProperties() {
        return properties;
    }

    public AbstractThriftServerContext setProperties(ThriftServerProperties properties) {
        this.properties = properties;
        return this;
    }

    public List<ThriftServiceWrapper> getServiceWrappers() {
        return serviceWrappers;
    }

    public AbstractThriftServerContext setServiceWrappers(List<ThriftServiceWrapper> serviceWrappers) {
        this.serviceWrappers = serviceWrappers;
        return this;
    }

}
