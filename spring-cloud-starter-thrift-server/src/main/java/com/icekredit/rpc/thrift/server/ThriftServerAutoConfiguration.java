package com.icekredit.rpc.thrift.server;

import com.icekredit.rpc.thrift.server.annotation.ThriftService;
import com.icekredit.rpc.thrift.server.context.AbstractThriftServerContext;
import com.icekredit.rpc.thrift.server.context.ThriftServerContext;
import com.icekredit.rpc.thrift.server.exception.ThriftServerException;
import com.icekredit.rpc.thrift.server.exception.ThriftServerInstantiateException;
import com.icekredit.rpc.thrift.server.properties.ThriftServerProperties;
import com.icekredit.rpc.thrift.server.properties.ThriftServerPropertiesCondition;
import com.icekredit.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import com.icekredit.rpc.thrift.server.wrapper.ThriftServiceWrapperFactory;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(value = "spring.thrift.server.service-id",matchIfMissing = false)
@EnableConfigurationProperties(ThriftServerProperties.class)
public class ThriftServerAutoConfiguration implements ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public ThriftServerGroup thriftServerGroup(ThriftServerProperties properties) throws TTransportException, IOException {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(ThriftService.class);
        if (Objects.isNull(beanNames) || beanNames.length == 0) {
            log.error("Can not found any thrift service annotated with @ThriftService");
            throw new ThriftServerException("Can not found any thrift service");
        }

        List<ThriftServiceWrapper> serviceWrappers = Arrays.stream(beanNames).distinct().map(beanName -> {
            Object bean = applicationContext.getBean(beanName);
            Object target = bean;

            ThriftService thriftService = bean.getClass().getAnnotation(ThriftService.class);
            String thriftServiceName = StringUtils.isEmpty(thriftService.value()) ? beanName : thriftService.value();

            if(target instanceof Advised) {
                final Object targetBean = target;
                TargetSource targetSource = ((Advised) target).getTargetSource();
                if (log.isDebugEnabled()) {
                    log.debug("Target object {} uses cglib proxy");
                }

                try {
                    target = targetSource.getTarget();
                } catch (Exception e) {
                    throw new ThriftServerInstantiateException("Failed to get target bean from " + target, e);
                }

                return ThriftServiceWrapperFactory.wrapper(
                        properties.getServiceId(),
                        thriftServiceName, targetBean,
                        thriftService.version());
            }

            return ThriftServiceWrapperFactory.wrapper(
                    properties.getServiceId(),
                    thriftServiceName, target,
                    thriftService.version());

        }).collect(Collectors.toList());

        AbstractThriftServerContext serverContext = new ThriftServerContext(properties, serviceWrappers);
        return new ThriftServerGroup(serverContext.buildServer());
    }

    @Bean
    @ConditionalOnMissingBean
    public ThriftServerBootstrap thriftServerBootstrap(ThriftServerGroup thriftServerGroup) {
        return new ThriftServerBootstrap(thriftServerGroup);
    }

}
