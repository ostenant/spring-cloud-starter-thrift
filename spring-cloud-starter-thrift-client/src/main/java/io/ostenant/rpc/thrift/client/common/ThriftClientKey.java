package io.ostenant.rpc.thrift.client.common;

public class ThriftClientKey {

    private String signature;

    private ThriftServerNode serverNode;

    public ThriftClientKey(String signature, ThriftServerNode serverNode) {
        this.signature = signature;
        this.serverNode = serverNode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ThriftServerNode getServerNode() {
        return serverNode;
    }

    public void setServerNode(ThriftServerNode serverNode) {
        this.serverNode = serverNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThriftClientKey that = (ThriftClientKey) o;

        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;
        return serverNode != null ? serverNode.equals(that.serverNode) : that.serverNode == null;
    }

    @Override
    public int hashCode() {
        int result = signature != null ? signature.hashCode() : 0;
        result = 31 * result + (serverNode != null ? serverNode.hashCode() : 0);
        return result;
    }
}
