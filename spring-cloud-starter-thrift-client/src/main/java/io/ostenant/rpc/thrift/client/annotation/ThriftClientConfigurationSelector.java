package io.ostenant.rpc.thrift.client.annotation;

import io.ostenant.rpc.thrift.client.ThriftClientAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ThriftClientConfigurationSelector implements ImportSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientConfigurationSelector.class);

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String importClassName = ThriftClientAutoConfiguration.class.getName();
        LOGGER.info("Auto import configuration class {}", importClassName);

        return new String[]{importClassName};
    }
}
