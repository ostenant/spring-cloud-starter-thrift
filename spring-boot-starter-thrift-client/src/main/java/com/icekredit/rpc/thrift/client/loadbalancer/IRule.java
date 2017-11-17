package com.icekredit.rpc.thrift.client.loadbalancer;

import com.icekredit.rpc.thrift.client.common.ThriftServerNode;

public interface IRule {

    public ThriftServerNode choose(String key);

    public void setLoadBalancer(ILoadBalancer lb);

    public ILoadBalancer getLoadBalancer();

}
