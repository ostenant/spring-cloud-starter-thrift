package io.ostenant.rpc.thrift.server;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import io.ostenant.rpc.thrift.server.properties.ThriftServerDiscoveryProperties;
import io.ostenant.rpc.thrift.server.properties.ThriftServerHealthCheckProperties;
import io.ostenant.rpc.thrift.server.properties.ThriftServerProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

@Configuration
@AutoConfigureAfter(ThriftServerAutoConfiguration.class)
@ConditionalOnProperty(name = "spring.thrift.server.discovery.enabled", havingValue = "true")
@Import(ThriftServerAutoConfiguration.class)
public class ThriftServerDiscoveryConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftServerDiscoveryConfiguration.class);

    private static final String REGISTRY_URL_TEMPLATE = "http://%s:%d";
    private static final String HEALTH_CHECK_URL_TEMPLATE = "%s:%d";

    @Bean
    public Consul thriftConsulClient(ThriftServerProperties thriftServerProperties) throws UnknownHostException {
        ThriftServerDiscoveryProperties discoveryProperties = thriftServerProperties.getDiscovery();
        String discoveryHostAddress = discoveryProperties.getHost();
        Integer discoveryPort = discoveryProperties.getPort();
        LOGGER.info("Service discovery server host {}, port {}", discoveryHostAddress, discoveryPort);

        String serviceName = thriftServerProperties.getServiceId();
        String serverHostAddress = Inet4Address.getLocalHost().getHostAddress();
        int servicePort = thriftServerProperties.getPort();
        String serviceId = String.join(":", serviceName, serverHostAddress, String.valueOf(servicePort));

        LOGGER.info("Service id {}", serviceId);
        LOGGER.info("Service name {}", serviceName);
        LOGGER.info("Service host address {}, port {}", serverHostAddress, servicePort);

        List<String> serviceTags = discoveryProperties.getTags();
        if (CollectionUtils.isNotEmpty(serviceTags)) {
            LOGGER.info("Service tags [{}]", String.join(", ", serviceTags));
        }

        String discoveryUrl = String.format(REGISTRY_URL_TEMPLATE, discoveryHostAddress, discoveryPort);
        Consul consulClient = Consul.builder().withUrl(discoveryUrl).build();
        registerAgentService(discoveryProperties, serviceName, serverHostAddress, servicePort,
                serviceId, serviceTags, consulClient);
        return consulClient;
    }

    private void registerAgentService(ThriftServerDiscoveryProperties discoveryProperties, String serviceName,
                                      String serverHostAddress, int servicePort, String serviceId,
                                      List<String> serviceTags, Consul consulClient) {
        AgentClient agentClient = consulClient.agentClient();
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
        builder.id(serviceId).name(serviceName).address(serverHostAddress)
                .port(servicePort).tags(serviceTags).enableTagOverride(false);
        registerHealthCheck(discoveryProperties, serverHostAddress, servicePort, builder);
        agentClient.register(builder.build());
    }

    private void registerHealthCheck(ThriftServerDiscoveryProperties discoveryProperties, String serverHostAddress,
                                     int servicePort, ImmutableRegistration.Builder builder) {
        ThriftServerHealthCheckProperties healthCheckProperties = discoveryProperties.getHealthCheck();
        Boolean healthCheckEnabled = healthCheckProperties.getEnabled();
        String healthCheckUrl = String.format(HEALTH_CHECK_URL_TEMPLATE, serverHostAddress, servicePort);

        if (healthCheckEnabled) {
            Long checkInterval = healthCheckProperties.getCheckInterval();
            Long checkTimeout = healthCheckProperties.getCheckTimeout();
            LOGGER.info("Service health check tcp url {}", healthCheckUrl);
            LOGGER.info("Service health check interval {}s, timeout {}s", checkInterval, checkTimeout);
            Registration.RegCheck regCheck = ImmutableRegCheck.tcp(healthCheckUrl, checkInterval, checkTimeout);
            builder.check(regCheck);
        }
    }
}
