package com.icekredit.rpc.thrift.client.loadbalancer;

import com.icekredit.rpc.thrift.client.common.ThriftServerNode;
import com.icekredit.rpc.thrift.client.discovery.ServerListUpdater;
import com.icekredit.rpc.thrift.client.discovery.ThriftConsulServerListUpdater;
import com.icekredit.rpc.thrift.client.discovery.ThriftConsulServerNode;
import com.icekredit.rpc.thrift.client.discovery.ThriftConsulServerNodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftConsulServerListLoadBalancer extends AbstractLoadBalancer {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ThriftConsulServerNodeList serverNodeList;
    private IRule rule;

    private volatile ServerListUpdater serverListUpdater;

    private final ServerListUpdater.UpdateAction updateAction = this::updateListOfServers;

    public ThriftConsulServerListLoadBalancer(ThriftConsulServerNodeList serverNodeList, IRule rule) {
        this.serverNodeList = serverNodeList;
        this.rule = rule;
        this.serverListUpdater = new ThriftConsulServerListUpdater();
        this.startUpdateAction();
    }

    @Override
    public ThriftConsulServerNodeList getThriftServerNodeList() {
        return this.serverNodeList;
    }

    @Override
    public ThriftConsulServerNode chooseServerNode(String key) {
        if (rule == null) {
            return null;
        } else {
            ThriftServerNode serverNode;
            try {
                serverNode = rule.choose(key);
            } catch (Exception e) {
                log.warn("LoadBalancer [{}]:  Error choosing server for key {}", getClass().getSimpleName(), key, e);
                return null;
            }

            if (serverNode != null && serverNode instanceof ThriftConsulServerNode) {
                return (ThriftConsulServerNode) serverNode;
            }
        }

        return null;
    }

    private synchronized void startUpdateAction() {
        log.info("Using serverListUpdater {}", serverListUpdater.getClass().getSimpleName());
        if (serverListUpdater == null) {
            serverListUpdater = new ThriftConsulServerListUpdater();
        }

        this.serverListUpdater.start(updateAction);
    }

    public void stopServerListRefreshing() {
        if (serverListUpdater != null) {
            serverListUpdater.stop();
        }
    }

    private void updateListOfServers() {
        this.serverNodeList.refreshThriftServers();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ThriftConsulServerListLoadBalancer:");
        sb.append(super.toString());
        sb.append("ServerList:").append(String.valueOf(serverNodeList));
        return sb.toString();
    }

}
