package io.ostenant.rpc.thrift.client.loadbalancer;

import io.ostenant.rpc.thrift.client.common.ThriftServerNode;

public interface IRule {

    public ThriftServerNode choose(String key);

    public void setLoadBalancer(ILoadBalancer lb);

    public ILoadBalancer getLoadBalancer();

}
