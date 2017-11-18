package com.icekredit.rpc.thrift.client.properties;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class ThriftClientPropertiesCondition extends SpringBootCondition {

    private static final String SPRING_THRIFT_CLIENT = "spring.thrift.client.";
    private static final String SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN = "package-to-scan";
    private static final String SPRING_THRIFT_CLIENT_SERVICE_MODEL = "service-model";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment());
        Map<String, Object> properties = resolver.getSubProperties(SPRING_THRIFT_CLIENT);

        return new ConditionOutcome(MapUtils.isNotEmpty(properties)
                && properties.containsKey(SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN)
                && StringUtils.isNotBlank(MapUtils.getString(properties, SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN)
        ), "Thrift server service model is " + MapUtils.getString(properties, SPRING_THRIFT_CLIENT_SERVICE_MODEL));

    }

}
