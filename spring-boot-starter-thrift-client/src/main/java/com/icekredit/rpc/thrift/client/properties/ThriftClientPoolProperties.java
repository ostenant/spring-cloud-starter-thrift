package com.icekredit.rpc.thrift.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.thrift.client.pool")
public class ThriftClientPoolProperties {

    private int retryTimes = 3;

    private int poolMaxTotalPerKey = 200;

    private int poolMaxIdlePerKey = 40;

    private int poolMinIdlePerKey = 10;

    private long poolMaxWait = 1000;

    /**
     * 池对象创建时时验证是否正常可用
     */
    private boolean testOnCreate = true;

    /**
     * 池对象借出时验证是否正常可用
     */
    private boolean testOnBorrow = true;


    /**
     * 池对象归还时验证是否正常可用
     */
    private boolean testOnReturn = true;

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getPoolMaxTotalPerKey() {
        return poolMaxTotalPerKey;
    }

    public void setPoolMaxTotalPerKey(int poolMaxTotalPerKey) {
        this.poolMaxTotalPerKey = poolMaxTotalPerKey;
    }

    public int getPoolMaxIdlePerKey() {
        return poolMaxIdlePerKey;
    }

    public void setPoolMaxIdlePerKey(int poolMaxIdlePerKey) {
        this.poolMaxIdlePerKey = poolMaxIdlePerKey;
    }

    public int getPoolMinIdlePerKey() {
        return poolMinIdlePerKey;
    }

    public void setPoolMinIdlePerKey(int poolMinIdlePerKey) {
        this.poolMinIdlePerKey = poolMinIdlePerKey;
    }

    public long getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(long poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
}
