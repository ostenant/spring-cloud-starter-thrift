package com.icekredit.rpc.thrift.server.argument;

import com.icekredit.rpc.thrift.server.exception.ThriftServerException;
import com.icekredit.rpc.thrift.server.processor.TRegisterProcessor;
import com.icekredit.rpc.thrift.server.processor.TRegisterProcessorFactory;
import com.icekredit.rpc.thrift.server.properties.TThreadPoolServerProperties;
import com.icekredit.rpc.thrift.server.properties.TThreadedSelectorServerProperties;
import com.icekredit.rpc.thrift.server.properties.ThriftServerProperties;
import com.icekredit.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TThreadPoolServerArgument extends TThreadPoolServer.Args {

    private Map<String, ThriftServiceWrapper> processorMap = new HashMap<>();

    public TThreadPoolServerArgument(List<ThriftServiceWrapper> serviceWrappers, ThriftServerProperties properties)
            throws TTransportException, IOException {
        super(new TServerSocket(new ServerSocket(properties.getPort())));

        transportFactory(new TTransportFactory());
        protocolFactory(new TBinaryProtocol.Factory());

        TThreadPoolServerProperties threadPoolProperties = properties.getThreadPool();

        minWorkerThreads(threadPoolProperties.getMinWorkerThreads());
        maxWorkerThreads(threadPoolProperties.getMaxWorkerThreads());
        requestTimeout(threadPoolProperties.getRequestTimeout());

        executorService(createInvokerPool(properties));

        try {
            TRegisterProcessor registerProcessor = TRegisterProcessorFactory.registerProcessor(serviceWrappers);

            processorMap.clear();
            processorMap.putAll(registerProcessor.getProcessorMap());

            processor(registerProcessor);
        } catch (Exception e) {
            throw new ThriftServerException("Can not create multiplexed processor for " + serviceWrappers, e);
        }
    }


    private ExecutorService createInvokerPool(ThriftServerProperties properties) {
        TThreadedSelectorServerProperties threadedSelectorProperties = properties.getThreadedSelector();

        return new ThreadPoolExecutor(
                threadedSelectorProperties.getMinWorkerThreads(),
                threadedSelectorProperties.getMaxWorkerThreads(),
                threadedSelectorProperties.getKeepAlivedTime(), TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(properties.getWorkerQueueCapacity()));
    }

    public Map<String, ThriftServiceWrapper> getProcessorMap() {
        return processorMap;
    }

}
