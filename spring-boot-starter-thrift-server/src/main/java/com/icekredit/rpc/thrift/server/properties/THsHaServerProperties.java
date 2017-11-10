package com.icekredit.rpc.thrift.server.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnProperty(prefix = "spring.thrift.server", name = "serviceModel", havingValue = "hsHa")
@ConfigurationProperties(prefix = "spring.thrift.server.hs-ha")
public class THsHaServerProperties {

    /**
     * 最少工作线程数量
     */
    private int minWorkerThreads = 5;

    /**
     * 最多工作线程数量
     */
    private int maxWorkerThreads = 20;

    /**
     * 线程的存活时间
     */
    private int keepAlivedTime = 1;

    public int getMinWorkerThreads() {
        return minWorkerThreads;
    }

    public void setMinWorkerThreads(int minWorkerThreads) {
        this.minWorkerThreads = minWorkerThreads;
    }

    public int getMaxWorkerThreads() {
        return maxWorkerThreads;
    }

    public void setMaxWorkerThreads(int maxWorkerThreads) {
        this.maxWorkerThreads = maxWorkerThreads;
    }

    public int getKeepAlivedTime() {
        return keepAlivedTime;
    }

    public void setKeepAlivedTime(int keepAlivedTime) {
        this.keepAlivedTime = keepAlivedTime;
    }
}
