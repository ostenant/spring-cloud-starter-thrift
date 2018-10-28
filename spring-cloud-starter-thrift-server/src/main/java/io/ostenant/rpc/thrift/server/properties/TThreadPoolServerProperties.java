package io.ostenant.rpc.thrift.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.thrift.server.thread-pool")
public class TThreadPoolServerProperties {

    /**
     * 最少工作线程的数量
     */
    private int minWorkerThreads = 5;

    /**
     * 最大工作线程的数量
     */
    private int maxWorkerThreads = 20;

    /**
     * 线程请求超时时间
     */
    private int requestTimeout = 5;

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

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getKeepAlivedTime() {
        return keepAlivedTime;
    }

    public void setKeepAlivedTime(int keepAlivedTime) {
        this.keepAlivedTime = keepAlivedTime;
    }
}
