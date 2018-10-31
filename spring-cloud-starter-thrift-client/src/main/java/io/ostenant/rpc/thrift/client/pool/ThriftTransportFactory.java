package io.ostenant.rpc.thrift.client.pool;

import io.ostenant.rpc.thrift.client.common.ThriftServerNode;
import io.ostenant.rpc.thrift.client.exception.ThriftClientConfigException;
import io.ostenant.rpc.thrift.client.properties.TServiceModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThriftTransportFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftTransportFactory.class);

    private static final int CONNECT_TIMEOUT = 10;

    public static TTransport determineTTranport(String serviceModel, ThriftServerNode serverNode, int connectTimeout) {
        TTransport transport;

        switch (serviceModel) {
            case TServiceModel.SERVICE_MODEL_SIMPLE:
            case TServiceModel.SERVICE_MODEL_THREAD_POOL:
                transport = createTSocket(serviceModel, serverNode, connectTimeout);
                break;

            case TServiceModel.SERVICE_MODEL_NON_BLOCKING:
            case TServiceModel.SERVICE_MODEL_HS_HA:
            case TServiceModel.SERVICE_MODEL_THREADED_SELECTOR:
                transport = createTFramedTransport(serviceModel, serverNode, connectTimeout);
                break;

            default:
                throw new ThriftClientConfigException("Service model is configured in wrong way");
        }

        return transport;
    }

    public static TTransport determineTTranport(String serviceModel, ThriftServerNode serverNode) {
        return determineTTranport(serviceModel, serverNode, CONNECT_TIMEOUT);
    }

    private static TTransport createTSocket(String serviceModel, ThriftServerNode serverNode, int connectTimeout) {
        TTransport transport = new TSocket(serverNode.getHost(), serverNode.getPort(),
                connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT);
        LOGGER.info("Established a new socket transport, service model is {}", serviceModel);
        return transport;
    }

    private static TTransport createTFramedTransport(String serviceModel, ThriftServerNode serverNode, int connectTimeout) {
        TTransport transport = new TFastFramedTransport(new TSocket(serverNode.getHost(), serverNode.getPort(),
                connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT));
        LOGGER.info("Established a new framed transport, service model is {}", serviceModel);
        return transport;
    }

}
