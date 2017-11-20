package com.icekredit.rpc.thrift.server.processor;

import com.icekredit.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;

import java.util.HashMap;
import java.util.Map;

public class TRegisterProcessor extends TMultiplexedProcessor {

    protected volatile Map<String, ThriftServiceWrapper> processorMetaMap;

    protected TRegisterProcessor() {
    }

    @Override
    public void registerProcessor(String serviceName, TProcessor processor) {
        super.registerProcessor(serviceName, processor);
    }

    public Map<String, ThriftServiceWrapper> getProcessorMap() {
        return new HashMap<>(processorMetaMap);
    }

    void setProcessorMap(Map<String, ThriftServiceWrapper> processorMetaMap) {
        this.processorMetaMap = processorMetaMap;
    }
}
