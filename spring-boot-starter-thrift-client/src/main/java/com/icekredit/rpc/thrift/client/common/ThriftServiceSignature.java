package com.icekredit.rpc.thrift.client.common;

public class ThriftServiceSignature {

    private String thriftServiceId;

    private Class<?> referClass;

    private double version;

    public ThriftServiceSignature(String thriftServiceId, Class<?> referClass, double version) {
        this.thriftServiceId = thriftServiceId;
        this.referClass = referClass;
        this.version = version;
    }

    public String getThriftServiceId() {
        return thriftServiceId;
    }

    public void setThriftServiceId(String thriftServiceId) {
        this.thriftServiceId = thriftServiceId;
    }

    public Class<?> getReferClass() {
        return referClass;
    }

    public void setReferClass(Class<?> referClass) {
        this.referClass = referClass;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }
}
