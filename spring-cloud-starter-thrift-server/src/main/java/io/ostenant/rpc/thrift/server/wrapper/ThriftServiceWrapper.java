package io.ostenant.rpc.thrift.server.wrapper;

public class ThriftServiceWrapper {

    private String thriftServiceName;

    private String thriftServiceSignature;

    private Class<?> type;

    private Class<?> ifaceType;

    private double version;

    private final Object thriftService;

    private final static Double DEFAULT_VERSION = 1.0;

    public ThriftServiceWrapper(String thriftServiceName, Class<?> type, Object thriftService) {
        this(thriftServiceName, type, thriftService, DEFAULT_VERSION);
    }

    public ThriftServiceWrapper(String thriftServiceName, Class<?> type, Object thriftService, double version) {
        this.thriftServiceName = thriftServiceName;
        this.type = type;
        this.thriftService = thriftService;
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

    public Class<?> getIfaceType() {
        return ifaceType;
    }

    public void setIfaceType(Class<?> ifaceType) {
        this.ifaceType = ifaceType;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public Object getThriftService() {
        return thriftService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThriftServiceWrapper that = (ThriftServiceWrapper) o;

        return thriftServiceSignature != null ? thriftServiceSignature.equals(that.thriftServiceSignature) : that.thriftServiceSignature == null;
    }

    @Override
    public int hashCode() {
        return thriftServiceSignature != null ? thriftServiceSignature.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ThriftServiceWrapper").append("{");
        builder.append("thriftServiceName=").append(thriftServiceName).append(",");
        builder.append("thriftServiceSignature=").append(thriftServiceSignature).append(",");
        builder.append("type=").append(type).append(",");
        builder.append("ifaceType=").append(ifaceType).append(",");
        builder.append("version=").append(version).append(",");
        builder.append("thriftService=").append(thriftService);

        return builder.append("}").toString();
    }
}
