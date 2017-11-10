package com.icekredit.rpc.thrift.client.common;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class ThriftServerCluster {

    private Map<String, LinkedHashSet<ThriftServerNode>> serverCluster = new LinkedHashMap<>();

    public Map<String, LinkedHashSet<ThriftServerNode>> getServerCluster() {
        Map<String, LinkedHashSet<ThriftServerNode>> cluster = new LinkedHashMap<>(serverCluster);
        return cluster;
    }

}
