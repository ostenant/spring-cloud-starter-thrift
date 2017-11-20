package com.icekredit.rpc.thrift.client.properties;

import com.icekredit.rpc.thrift.client.common.ThriftClientContext;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class ConsulPropertiesCondition extends SpringBootCondition {

    private static final String SPRING_CLOUD_CONSUL = "spring.cloud.consul.";
    private static final String SPRING_CLOUD_CONSUL_HOST = "host";
    private static final String SPRING_CLOUD_CONSUL_PORT = "port";
    private static final String ADDRESS_TEMPLATE = "%s:%d";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment());
        Map<String, Object> properties = resolver.getSubProperties(SPRING_CLOUD_CONSUL);

        final String consulAddress = String.format(ADDRESS_TEMPLATE,
                MapUtils.getString(properties, SPRING_CLOUD_CONSUL_HOST),
                MapUtils.getInteger(properties, SPRING_CLOUD_CONSUL_PORT));

        ThriftClientContext.registry(consulAddress);

        return new ConditionOutcome(MapUtils.isNotEmpty(properties)
                && properties.containsKey(SPRING_CLOUD_CONSUL_HOST)
                && StringUtils.isNotBlank(MapUtils.getString(properties, SPRING_CLOUD_CONSUL_HOST))
                && properties.containsKey(SPRING_CLOUD_CONSUL_PORT)
                && MapUtils.getInteger(properties, SPRING_CLOUD_CONSUL_PORT) > 0
                , "Consul server address is " + consulAddress);
    }

}
