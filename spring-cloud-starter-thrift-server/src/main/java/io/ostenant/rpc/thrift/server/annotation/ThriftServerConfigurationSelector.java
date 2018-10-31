package io.ostenant.rpc.thrift.server.annotation;

import io.ostenant.rpc.thrift.server.ThriftServerAutoConfiguration;
import io.ostenant.rpc.thrift.server.ThriftServerDiscoveryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ThriftServerConfigurationSelector implements ImportSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftServerConfigurationSelector.class);

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String autoClassName = ThriftServerAutoConfiguration.class.getName();
        String discoveryClassName = ThriftServerDiscoveryConfiguration.class.getName();
        LOGGER.info("Auto import configuration class {}, {}", autoClassName, discoveryClassName);

        return new String[]{autoClassName, discoveryClassName};
    }
}
