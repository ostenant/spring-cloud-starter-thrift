package io.ostenant.rpc.thrift.client.common;

import com.google.common.collect.Maps;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public abstract class ThriftServerNodeList<T extends ThriftServerNode> implements ServerNodeList<T> {

    protected Map<String, LinkedHashSet<T>> serverNodeMap = Maps.newConcurrentMap();

    public Map<String, LinkedHashSet<T>> getServerNodeMap() {
        return serverNodeMap;
    }

    @Override
    public Map<String, LinkedHashSet<T>> getInitialListOfThriftServers() {
        return getThriftServers();
    }

    @Override
    public Map<String, LinkedHashSet<T>> getUpdatedListOfThriftServers() {
        return refreshThriftServers();
    }

    public abstract List<T> getThriftServer(String serviceName);

    public abstract List<T> refreshThriftServer(String serviceName);

    public abstract Map<String, LinkedHashSet<T>> getThriftServers();

    public abstract Map<String, LinkedHashSet<T>> refreshThriftServers();

}
