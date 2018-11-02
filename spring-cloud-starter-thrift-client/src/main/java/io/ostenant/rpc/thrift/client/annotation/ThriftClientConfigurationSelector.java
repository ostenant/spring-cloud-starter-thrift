package io.ostenant.rpc.thrift.client.annotation;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.commons.util.SpringFactoryImportSelector;
import org.springframework.core.env.Environment;

import java.util.Set;

public class ThriftClientConfigurationSelector extends SpringFactoryImportSelector<EnableThriftClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientConfigurationSelector.class);

    private static final Set<String> SERVICE_MODEL_SET = Sets.newHashSet("simple",
            "nonBlocking", "threadPool", "hsHa", "threadedSelector");
    private static final String SERVICE_MODEL = "spring.thrift.client.service-model";

    @Override
    protected boolean isEnabled() {
        Environment environment = getEnvironment();
        String serviceModel = environment.getProperty(SERVICE_MODEL, String.class);
        boolean enableAutoConfiguration = SERVICE_MODEL_SET.contains(serviceModel);
        if (enableAutoConfiguration) {
            LOGGER.info("Enable thrift client auto configuration, service model {}", serviceModel);
        }
        return enableAutoConfiguration;
    }
}
