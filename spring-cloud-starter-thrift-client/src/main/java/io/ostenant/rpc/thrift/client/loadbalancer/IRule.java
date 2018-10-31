package io.ostenant.rpc.thrift.client.loadbalancer;

import io.ostenant.rpc.thrift.client.common.ThriftServerNode;

public interface IRule {

    ThriftServerNode choose(String key);

    void setLoadBalancer(ILoadBalancer lb);

    ILoadBalancer getLoadBalancer();

}
