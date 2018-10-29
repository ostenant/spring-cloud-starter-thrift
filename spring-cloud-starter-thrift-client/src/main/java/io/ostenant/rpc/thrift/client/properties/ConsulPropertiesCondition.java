package io.ostenant.rpc.thrift.client.properties;

import io.ostenant.rpc.thrift.client.common.ThriftClientContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConsulPropertiesCondition extends SpringBootCondition {

    private static final String SPRING_CLOUD_CONSUL_HOST = "spring.cloud.consul.host";
    private static final String SPRING_CLOUD_CONSUL_PORT = "spring.cloud.consul.port";
    private static final String ADDRESS_TEMPLATE = "%s:%d";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Environment environment = context.getEnvironment();
        String host = environment.getProperty(SPRING_CLOUD_CONSUL_HOST);
        int port = environment.getProperty(SPRING_CLOUD_CONSUL_PORT, int.class);
        String consulAddress = String.format(ADDRESS_TEMPLATE, host, port);

        ThriftClientContext.registry(consulAddress);

        return new ConditionOutcome(StringUtils.isNotBlank(host) && port > 0,
                "Consul server address is " + consulAddress);
    }
}
