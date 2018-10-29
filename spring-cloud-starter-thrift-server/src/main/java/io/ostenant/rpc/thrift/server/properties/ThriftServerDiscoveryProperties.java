package io.ostenant.rpc.thrift.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "spring.thrift.server.discovery")
public class ThriftServerDiscoveryProperties {

    /**
     * 是否允许服务注册
     */
    private Boolean enabled = false;

    /**
     * 服务注册中心的地址
     */
    private String host;

    /**
     * 服务注册中心的端口号(默认8500)
     */
    private Integer port = 8500;

    /**
     * 服务标签
     */
    private List<String> tags = new ArrayList<>();

    /**
     * 服务健康检查
     */
    private ThriftServerHealthCheckProperties healthCheck;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ThriftServerHealthCheckProperties getHealthCheck() {
        return healthCheck;
    }

    public void setHealthCheck(ThriftServerHealthCheckProperties healthCheck) {
        this.healthCheck = healthCheck;
    }
}
