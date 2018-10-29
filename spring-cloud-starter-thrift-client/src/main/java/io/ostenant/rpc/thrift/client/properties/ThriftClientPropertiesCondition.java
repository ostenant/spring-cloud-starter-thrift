package io.ostenant.rpc.thrift.client.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


public class ThriftClientPropertiesCondition extends SpringBootCondition {

    private static final String SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN = "spring.thrift.client.package-to-scan";
    private static final String SPRING_THRIFT_CLIENT_SERVICE_MODEL = "spring.thrift.client.service-model";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String packageToScan = context.getEnvironment().getProperty(SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN);
        String serviceModel = context.getEnvironment().getProperty(SPRING_THRIFT_CLIENT_SERVICE_MODEL);

        return new ConditionOutcome(StringUtils.isNotBlank(packageToScan)
                , "Thrift server service model is " + serviceModel);
    }

}
