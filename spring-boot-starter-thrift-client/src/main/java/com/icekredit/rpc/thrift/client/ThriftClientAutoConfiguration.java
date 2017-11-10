package com.icekredit.rpc.thrift.client;

import com.icekredit.rpc.thrift.client.properties.ThriftClientProperties;
import com.icekredit.rpc.thrift.client.properties.ThriftClientPropertiesCondition;
import com.icekredit.rpc.thrift.client.scanner.ThriftClientBeanScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(ThriftClientPropertiesCondition.class)
@EnableConfigurationProperties(ThriftClientProperties.class)
public class ThriftClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ThriftClientBeanScannerConfigurer thriftClientBeanScannerConfigurer(ThriftClientProperties properties) {
        return new ThriftClientBeanScannerConfigurer(properties);
    }

}
