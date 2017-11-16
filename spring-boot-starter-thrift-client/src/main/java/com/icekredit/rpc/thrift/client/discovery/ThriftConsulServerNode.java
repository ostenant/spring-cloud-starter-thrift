package com.icekredit.rpc.thrift.client.discovery;

import com.icekredit.rpc.thrift.client.common.ThriftServerNode;

import java.util.List;

public class ThriftConsulServerNode extends ThriftServerNode {

    private String node;

    private String serviceId;

    private List<String> tags;

    private String host;

    private int port;

    private String address;

    private boolean isHealth;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHealth() {
        return isHealth;
    }

    public void setHealth(boolean health) {
        isHealth = health;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "ThriftServerNode{" +
                "node='" + node + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", tags=" + tags +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", address='" + address + '\'' +
                ", isHealth=" + isHealth +
                '}';
    }

}
