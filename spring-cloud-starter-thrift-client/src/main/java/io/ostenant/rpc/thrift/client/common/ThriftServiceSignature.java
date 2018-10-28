package io.ostenant.rpc.thrift.client.common;

public class ThriftServiceSignature {

    private String thriftServiceId;

    private Class<?> thriftServiceClass;

    private double version;

    public ThriftServiceSignature(String thriftServiceId, Class<?> thriftServiceClass, double version) {
        this.thriftServiceId = thriftServiceId;
        this.thriftServiceClass = thriftServiceClass;
        this.version = version;
    }

    public String getThriftServiceId() {
        return thriftServiceId;
    }

    public void setThriftServiceId(String thriftServiceId) {
        this.thriftServiceId = thriftServiceId;
    }

    public Class<?> getThriftServiceClass() {
        return thriftServiceClass;
    }

    public void setThriftServiceClass(Class<?> thriftServiceClass) {
        this.thriftServiceClass = thriftServiceClass;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String marker() {
        return String.join("$", new String[]{
                thriftServiceId, thriftServiceClass.getName(),
                String.valueOf(version)
        });
    }
}
