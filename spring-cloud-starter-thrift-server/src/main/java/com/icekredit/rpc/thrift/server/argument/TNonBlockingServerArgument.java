package com.icekredit.rpc.thrift.server.argument;

import com.icekredit.rpc.thrift.server.exception.ThriftServerException;
import com.icekredit.rpc.thrift.server.processor.TRegisterProcessor;
import com.icekredit.rpc.thrift.server.processor.TRegisterProcessorFactory;
import com.icekredit.rpc.thrift.server.properties.ThriftServerProperties;
import com.icekredit.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TNonBlockingServerArgument extends TNonblockingServer.Args {

    private Map<String, ThriftServiceWrapper> processorMap = new HashMap<>();

    public TNonBlockingServerArgument(List<ThriftServiceWrapper> serviceWrappers, ThriftServerProperties properties)
            throws TTransportException {
        super(new TNonblockingServerSocket(properties.getPort()));

        transportFactory(new TFramedTransport.Factory());
        protocolFactory(new TBinaryProtocol.Factory());

        try {
            TRegisterProcessor registerProcessor = TRegisterProcessorFactory.registerProcessor(serviceWrappers);

            processorMap.clear();
            processorMap.putAll(registerProcessor.getProcessorMap());

            processor(registerProcessor);
        } catch (Exception e) {
            throw new ThriftServerException("Can not create multiplexed processor for " + serviceWrappers, e);
        }
    }


    public Map<String, ThriftServiceWrapper> getProcessorMap() {
        return processorMap;
    }
}
