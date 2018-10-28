package io.ostenant.rpc.thrift.server.context;

import io.ostenant.rpc.thrift.server.properties.TServiceModel;
import io.ostenant.rpc.thrift.server.properties.ThriftServerProperties;
import io.ostenant.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractThriftServerContext {

    private final Lock thriftServerLock = new ReentrantLock();
    protected ThriftServerProperties properties;
    protected List<ThriftServiceWrapper> serviceWrappers;
    private volatile TServer thriftServer;

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

        switch (serviceModel) {
            case TServiceModel.SERVICE_MODEL_SIMPLE:
                thriftServer = buildTSimpleServer();
                break;
            case TServiceModel.SERVICE_MODEL_NON_BLOCKING:
                thriftServer = buildTNonBlockingServer();
                break;
            case TServiceModel.SERVICE_MODEL_THREAD_POOL:
                thriftServer = buildTThreadPoolServer();
                break;
            case TServiceModel.SERVICE_MODEL_HS_HA:
                thriftServer = buildTHsHaServer();
                break;
            case TServiceModel.SERVICE_MODEL_THREADED_SELECTOR:
                thriftServer = buildTThreadedSelectorServer();
                break;
            default:
                thriftServer = buildDefaultTServer();
                break;
        }

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
