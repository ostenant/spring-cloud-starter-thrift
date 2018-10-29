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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Configuration
@AutoConfigureAfter(ThriftServerAutoConfiguration.class)
@ConditionalOnProperty(name = "spring.thrift.server.discovery", havingValue = "true")
@Import(ThriftServerAutoConfiguration.class)
public class ThriftServerDiscoveryConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftServerDiscoveryConfiguration.class);

    private static final String URL_TEMPLATE = "http://%s:%d";

    @Bean
    public Consul consulClient(ThriftServerProperties thriftServerProperties) throws UnknownHostException {
        ThriftServerDiscoveryProperties discoveryProperties = thriftServerProperties.getDiscovery();

        String discoveryHostAddress = discoveryProperties.getHost();
        Integer discoveryPort = discoveryProperties.getPort();
        LOGGER.info("Discovery server host {}, port {}", discoveryHostAddress, discoveryPort);

        String serviceId = thriftServerProperties.getServiceId();
        LOGGER.info("Service id {}", serviceId);

        InetAddress serverHostAddress = Inet4Address.getLocalHost();
        int servicePort = thriftServerProperties.getPort();
        LOGGER.info("Service host address {}, port {}", serverHostAddress.getHostAddress(), servicePort);

        List<String> serviceTags = discoveryProperties.getTags();
        if (CollectionUtils.isNotEmpty(serviceTags)) {
            LOGGER.info("Service tags [ {} ]", StringUtils.join(", ", serviceTags));
        }

        String discoveryUrl = String.format(URL_TEMPLATE, discoveryHostAddress, discoveryPort);
        Consul consulClient = Consul.builder().withUrl(discoveryUrl).build();
        AgentClient agentClient = consulClient.agentClient();

        ImmutableRegistration.Builder registrationBuilder = ImmutableRegistration.builder();
        registrationBuilder.id(serviceId).name(serviceId).port(servicePort).tags(serviceTags);

        ThriftServerHealthCheckProperties healthCheckProperties = discoveryProperties.getHealthCheck();
        Boolean healthCheckEnabled = healthCheckProperties.getEnabled();
        if (healthCheckEnabled) {
            String tcpPath = healthCheckProperties.getCheckTcp();
            Long checkInterval = healthCheckProperties.getCheckInterval();
            Long checkTimeout = healthCheckProperties.getCheckTimeout();

            LOGGER.info("Service health check tcp path {}{}", discoveryUrl, tcpPath);
            LOGGER.info("Service health check interval {}s, timeout {}s", checkInterval, checkTimeout);

            Registration.RegCheck regCheck = ImmutableRegCheck.tcp(tcpPath, checkInterval, checkTimeout);
            registrationBuilder.check(regCheck);
        }

        agentClient.register(registrationBuilder.build());
        return consulClient;
    }
}
