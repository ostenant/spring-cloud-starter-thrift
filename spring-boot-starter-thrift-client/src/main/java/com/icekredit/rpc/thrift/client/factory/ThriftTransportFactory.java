package com.icekredit.rpc.thrift.client.factory;

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

    public static TTransport determineTTranport(String serviceModel, ThriftServerNode serverNode) {
        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_SIMPLE, serviceModel))
            return createTSocket(serviceModel, serverNode);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_THREAD_POOL, serviceModel))
            return createTSocket(serviceModel, serverNode);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_NON_BLOCKING, serviceModel))
            return createTFramedTransport(serviceModel, serverNode);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_HS_HA, serviceModel))
            return createTFramedTransport(serviceModel, serverNode);


        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_THREADED_SELECTOR, serviceModel))
            return createTFramedTransport(serviceModel, serverNode);

        if (StringUtils.equalsIgnoreCase(TServiceModel.SERVICE_MODEL_DEFAULT, serviceModel))
            return createTFramedTransport(serviceModel, serverNode);

        throw new ThriftClientConfigException("Service model is configured in wrong way");
    }

    private static TTransport createTSocket(String serviceModel, ThriftServerNode serverNode) {
        TTransport transport = new TSocket(serverNode.getHost(), serverNode.getPort(), serverNode.getTimeout());
        log.info("Established a new socket transport, service model is {}", serviceModel);
        return transport;
    }

    private static TTransport createTFramedTransport(String serviceModel, ThriftServerNode serverNode) {
        TTransport transport = new TFramedTransport(new TSocket(serverNode.getHost(), serverNode.getPort(), serverNode.getTimeout()));
        log.info("Established a new framed transport, service model is {}", serviceModel);
        return transport;
    }

}
