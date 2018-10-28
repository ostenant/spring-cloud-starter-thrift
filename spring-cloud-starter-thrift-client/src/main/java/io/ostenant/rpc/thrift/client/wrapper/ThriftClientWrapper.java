package io.ostenant.rpc.thrift.client.wrapper;

public class ThriftClientWrapper {

    private String thriftServiceName;

    private String thriftServiceSignature;

    private Class<?> type;

    private Class<?> clientType;

    private double version;

    private final Object thriftClient;

    private final static Double DEFAULT_VERSION = 1.0;

    public ThriftClientWrapper(String thriftServiceName, Class<?> type, Object thriftService) {
        this(thriftServiceName, type, thriftService, DEFAULT_VERSION);
    }

    public ThriftClientWrapper(String thriftServiceName, Class<?> type, Object thriftClient, double version) {
        this.thriftServiceName = thriftServiceName;
        this.type = type;
        this.thriftClient = thriftClient;
        this.version = version;
    }

    public String getThriftServiceName() {
        return thriftServiceName;
    }

    public void setThriftServiceName(String thriftServiceName) {
        this.thriftServiceName = thriftServiceName;
    }

    public String getThriftServiceSignature() {
        return thriftServiceSignature;
    }

    public void setThriftServiceSignature(String thriftServiceSignature) {
        this.thriftServiceSignature = thriftServiceSignature;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getClientType() {
        return clientType;
    }

    public void setClientType(Class<?> clientType) {
        this.clientType = clientType;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public Object getThriftClient() {
        return thriftClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThriftClientWrapper that = (ThriftClientWrapper) o;

        return thriftServiceSignature != null ? thriftServiceSignature.equals(that.thriftServiceSignature) : that.thriftServiceSignature == null;
    }

    @Override
    public int hashCode() {
        return thriftServiceSignature != null ? thriftServiceSignature.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ThriftClientWrapper").append("{");
        builder.append("thriftServiceName=").append(thriftServiceName).append(",");
        builder.append("thriftServiceSignature=").append(thriftServiceSignature).append(",");
        builder.append("type=").append(type).append(",");
        builder.append("clientType=").append(clientType).append(",");
        builder.append("version=").append(version).append(",");
        builder.append("thriftClient=").append(thriftClient);

        return builder.append("}").toString();
    }
}
