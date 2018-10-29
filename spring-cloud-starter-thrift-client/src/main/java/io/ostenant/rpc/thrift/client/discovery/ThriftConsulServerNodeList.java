package io.ostenant.rpc.thrift.client.discovery;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.orbitz.consul.CatalogClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.async.ConsulResponseCallback;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.health.Node;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import com.orbitz.consul.option.QueryOptions;
import io.ostenant.rpc.thrift.client.common.ThriftServerNodeList;
import io.ostenant.rpc.thrift.client.exception.ThriftClientException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class ThriftConsulServerNodeList extends ThriftServerNodeList<ThriftConsulServerNode> {

    private final Consul consul;
    private final HealthClient healthClient;
    private final CatalogClient catalogClient;

    private static ThriftConsulServerNodeList serverNodeList = null;

    public static ThriftConsulServerNodeList singleton(Consul consul) {
        if (serverNodeList == null) {
            synchronized (ThriftConsulServerNodeList.class) {
                if (serverNodeList == null) {
                    serverNodeList = new ThriftConsulServerNodeList(consul);
                }
            }
        }
        return serverNodeList;
    }

    private ThriftConsulServerNodeList(Consul consul) {
        this.consul = consul;
        this.healthClient = this.consul.healthClient();
        this.catalogClient = this.consul.catalogClient();
    }

    @Override
    public List<ThriftConsulServerNode> getThriftServer(String serviceName) {
        if (MapUtils.isNotEmpty(this.serverNodeMap) && (this.serverNodeMap.containsKey(serviceName))) {
            LinkedHashSet<ThriftConsulServerNode> serverNodeSet = this.serverNodeMap.get(serviceName);
            if (CollectionUtils.isNotEmpty(serverNodeSet)) {
                return Lists.newArrayList(serverNodeSet);
            }
        }

        return refreshThriftServer(serviceName);
    }

    @Override
    public List<ThriftConsulServerNode> refreshThriftServer(String serviceName) {
        List<ThriftConsulServerNode> serverNodeList = Lists.newArrayList();
        List<ServiceHealth> serviceHealthList = healthClient.getAllServiceInstances(serviceName).getResponse();

        filterAndCompoServerNodes(serverNodeList, serviceHealthList);

        if (CollectionUtils.isNotEmpty(serverNodeList)) {
            this.serverNodeMap.put(serviceName, Sets.newLinkedHashSet(serverNodeList));
        }

        return serverNodeList;
    }

    @Override
    public Map<String, LinkedHashSet<ThriftConsulServerNode>> getThriftServers() {
        if (MapUtils.isNotEmpty(this.serverNodeMap)) {
            return this.serverNodeMap;
        }

        return refreshThriftServers();
    }

    @Override
    public Map<String, LinkedHashSet<ThriftConsulServerNode>> refreshThriftServers() {
        Map<String, List<String>> catalogServiceMap = catalogClient.getServices(QueryOptions.BLANK).getResponse();
        if (MapUtils.isEmpty(catalogServiceMap)) {
            return this.serverNodeMap;
        }

        Map<String, LinkedHashSet<ThriftConsulServerNode>> serverNodeMap = Maps.newConcurrentMap();
        for (Map.Entry<String, List<String>> catalogServiceEntry : catalogServiceMap.entrySet()) {
            String serviceName = catalogServiceEntry.getKey();
            List<String> tags = catalogServiceEntry.getValue();

            if (CollectionUtils.isEmpty(tags)) {
                continue;
            }

            List<ServiceHealth> serviceHealthList = healthClient.getAllServiceInstances(serviceName).getResponse();
            LinkedHashSet<ThriftConsulServerNode> serverNodeSet = Sets.newLinkedHashSet();
            List<ThriftConsulServerNode> serverNodes = Lists.newArrayList(serverNodeSet);
            filterAndCompoServerNodes(serverNodes, serviceHealthList);

            if (CollectionUtils.isNotEmpty(serverNodeSet)) {
                serverNodeMap.put(serviceName, serverNodeSet);
            }
        }

        this.serverNodeMap.clear();
        this.serverNodeMap.putAll(serverNodeMap);
        return ImmutableMap.copyOf(this.serverNodeMap);
    }


    private static ThriftConsulServerNode getThriftConsulServerNode(ServiceHealth serviceHealth) {
        ThriftConsulServerNode serverNode = new ThriftConsulServerNode();

        Node node = serviceHealth.getNode();
        serverNode.setNode(node.getNode());

        Service service = serviceHealth.getService();
        serverNode.setAddress(service.getAddress());
        serverNode.setPort(service.getPort());
        serverNode.setHost(ThriftConsulServerUtils.findHost(serviceHealth));

        serverNode.setServiceId(service.getService());
        serverNode.setTags(service.getTags());
        serverNode.setHealth(ThriftConsulServerUtils.isPassingCheck(serviceHealth));

        return serverNode;
    }


    private void filterAndCompoServerNodes(List<ThriftConsulServerNode> serverNodeList, List<ServiceHealth> serviceHealthList) {
        for (ServiceHealth serviceHealth : serviceHealthList) {
            ThriftConsulServerNode serverNode = getThriftConsulServerNode(serviceHealth);
            if (serverNode == null) {
                continue;
            }

            if (!serverNode.isHealth()) {
                continue;
            }

            if (CollectionUtils.isEmpty(serverNode.getTags())) {
                continue;
            }
            serverNodeList.add(serverNode);
        }
    }

    private static class ThriftConsulResponseCallback implements ConsulResponseCallback<List<ServiceHealth>> {

        List<ThriftConsulServerNode> serverNodeList;

        public ThriftConsulResponseCallback(List<ThriftConsulServerNode> serverNodeList) {
            this.serverNodeList = serverNodeList;
        }

        @Override
        public void onComplete(ConsulResponse<List<ServiceHealth>> consulResponse) {
            List<ServiceHealth> response = consulResponse.getResponse();
            for (ServiceHealth serviceHealth : response) {
                ThriftConsulServerNode serverNode = getThriftConsulServerNode(serviceHealth);
                serverNodeList.add(serverNode);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            throw new ThriftClientException("Failed to query service instances from consul agent", throwable);
        }

    }

}
