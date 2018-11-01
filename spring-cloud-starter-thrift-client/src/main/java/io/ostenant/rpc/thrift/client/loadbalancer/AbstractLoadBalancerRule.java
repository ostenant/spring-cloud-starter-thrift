package io.ostenant.rpc.thrift.client.loadbalancer;

public abstract class AbstractLoadBalancerRule implements IRule {

    protected ILoadBalancer lb;

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        this.lb = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return lb;
    }
}
