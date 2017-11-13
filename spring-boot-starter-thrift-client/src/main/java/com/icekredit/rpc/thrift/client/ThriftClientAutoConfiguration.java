package com.icekredit.rpc.thrift.client;

import com.icekredit.rpc.thrift.client.pool.TransportKeyedObjectPool;
import com.icekredit.rpc.thrift.client.pool.TransportKeyedPooledObjectFactory;
import com.icekredit.rpc.thrift.client.properties.ThriftClientPoolProperties;
import com.icekredit.rpc.thrift.client.properties.ThriftClientProperties;
import com.icekredit.rpc.thrift.client.properties.ThriftClientPropertiesCondition;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
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
    public ThriftClientBeanScannerConfigurer thriftClientBeanScannerConfigurer() {
        return new ThriftClientBeanScannerConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ThriftClientBeanPostProcessor thriftClientBeanPostProcessor() {
        return new ThriftClientBeanPostProcessor();
    }

    @Bean
    public GenericKeyedObjectPoolConfig keyedObjectPoolConfig(ThriftClientProperties properties) {
        ThriftClientPoolProperties poolProperties = properties.getPool();

        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxIdlePerKey(poolProperties.getPoolMaxIdlePerKey());
        config.setMaxIdlePerKey(poolProperties.getPoolMaxIdlePerKey());
        config.setMaxWaitMillis(poolProperties.getPoolMaxWait());
        config.setMaxTotalPerKey(poolProperties.getPoolMaxTotalPerKey());
        config.setTestOnCreate(poolProperties.isTestOnCreate());
        config.setTestOnBorrow(poolProperties.isTestOnBorrow());
        config.setTestOnReturn(poolProperties.isTestOnReturn());
        config.setJmxEnabled(false);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public TransportKeyedPooledObjectFactory transportKeyedPooledObjectFactory(
            ThriftClientProperties properties) {
        return new TransportKeyedPooledObjectFactory(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransportKeyedObjectPool transportKeyedObjectPool(GenericKeyedObjectPoolConfig config,
                                                             TransportKeyedPooledObjectFactory poolFactory) {
        return new TransportKeyedObjectPool(poolFactory, config);
    }


}
