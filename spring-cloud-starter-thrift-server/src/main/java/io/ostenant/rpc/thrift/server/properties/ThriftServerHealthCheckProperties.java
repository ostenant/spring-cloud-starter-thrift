package io.ostenant.rpc.thrift.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.thrift.server.health-check")
public class ThriftServerHealthCheckProperties {

    /**
     * 是否允许健康检查
     */
    private Boolean enabled = true;

    /**
     * 服务健康检查路径
     */
    private String checkTcp = "/";

    /**
     * 服务健康检查时间间隔
     */
    private Long checkInterval = 30L;

    /**
     * 服务健康检查超时时间
     */
    private Long checkTimeout = 3L;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCheckTcp() {
        return checkTcp;
    }

    public void setCheckTcp(String checkTcp) {
        this.checkTcp = checkTcp;
    }

    public Long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(Long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public Long getCheckTimeout() {
        return checkTimeout;
    }

    public void setCheckTimeout(Long checkTimeout) {
        this.checkTimeout = checkTimeout;
    }
}
