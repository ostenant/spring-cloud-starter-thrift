package com.icekredit.rpc.thrift.client.factory;


import com.icekredit.rpc.thrift.client.common.ThriftServerCluster;

public class ThriftServerClusterFactory {

    private ThriftServerClusterFactory() {
    }

    public static ThriftServerCluster get() {
        return ThriftServerClusterHolder.THRIFT_SERVER_CLUSTER;
    }

    private final static class ThriftServerClusterHolder {
        private final static ThriftServerCluster THRIFT_SERVER_CLUSTER = new ThriftServerCluster();
    }

}
