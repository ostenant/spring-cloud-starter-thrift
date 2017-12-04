package com.icekredit.rpc.thrift.client.pool;

import com.icekredit.rpc.thrift.client.common.ThriftServerNode;
import com.icekredit.rpc.thrift.client.exception.ThriftClientConfigException;
import com.icekredit.rpc.thrift.client.properties.TServiceModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThriftTransportFactory {

    private static Logger log = LoggerFactory.getLogger(ThriftTransportFactory.class);

    private final static int CONNECT_TIMEOUT = 10;

    public static TTransport determineTTranport(String serviceModel, ThriftServerNode serverNode, int connectTimeout) {
        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_SIMPLE, serviceModel))
            return createTSocket(serviceModel, serverNode, connectTimeout);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_THREAD_POOL, serviceModel))
            return createTSocket(serviceModel, serverNode, connectTimeout);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_NON_BLOCKING, serviceModel))
            return createTFramedTransport(serviceModel, serverNode, connectTimeout);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_HS_HA, serviceModel))
            return createTFramedTransport(serviceModel, serverNode, connectTimeout);


        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_THREADED_SELECTOR, serviceModel))
            return createTFramedTransport(serviceModel, serverNode, connectTimeout);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_DEFAULT, serviceModel))
            return createTFramedTransport(serviceModel, serverNode, connectTimeout);

        throw new ThriftClientConfigException("Service model is configured in wrong way");
    }

    public static TTransport determineTTranport(String serviceModel, ThriftServerNode serverNode) {
        return determineTTranport(serviceModel, serverNode, CONNECT_TIMEOUT);
    }

    private static TTransport createTSocket(String serviceModel, ThriftServerNode serverNode, int connectTimeout) {
        TTransport transport = new TSocket(serverNode.getHost(), serverNode.getPort(), connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT);
        log.info("Established a new socket transport, service model is {}", serviceModel);
        return transport;
    }

    private static TTransport createTFramedTransport(String serviceModel, ThriftServerNode serverNode, int connectTimeout) {
        TTransport transport = new TFramedTransport(new TSocket(serverNode.getHost(), serverNode.getPort(),
                connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT));
        log.info("Established a new framed transport, service model is {}", serviceModel);
        return transport;
    }

}
