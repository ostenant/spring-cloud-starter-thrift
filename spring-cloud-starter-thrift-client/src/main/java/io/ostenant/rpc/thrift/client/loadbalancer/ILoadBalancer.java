package io.ostenant.rpc.thrift.client.loadbalancer;

import io.ostenant.rpc.thrift.client.common.ThriftServerNode;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public interface ILoadBalancer<T extends ThriftServerNode> {

    public T chooseServerNode(String key);

    public Map<String, LinkedHashSet<T>> getAllServerNodes();

    public Map<String, LinkedHashSet<T>> getRefreshedServerNodes();

    public List<T> getServerNodes(String key);

    public List<T> getRefreshedServerNodes(String key);

}
