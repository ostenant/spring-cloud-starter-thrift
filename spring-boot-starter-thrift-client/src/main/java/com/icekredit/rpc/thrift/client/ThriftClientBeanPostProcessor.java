package com.icekredit.rpc.thrift.client;

import com.icekredit.rpc.thrift.client.annotation.ThriftReferer;
import com.icekredit.rpc.thrift.client.exception.ThriftClientInstantiateException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Map;
import java.util.Optional;

public class ThriftClientBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object target = bean;
        if (AopUtils.isJdkDynamicProxy(target)) {
            TargetSource targetSource = ((Advised) target).getTargetSource();
            if (log.isDebugEnabled()) {
                log.debug("Target object {} uses jdk dynamic proxy");
            }

            try {
                target = targetSource.getTarget();
            } catch (Exception e) {
                throw new ThriftClientInstantiateException("Failed to get target bean from " + target, e);
            }
        }

        if (AopUtils.isCglibProxy(target)) {
            TargetSource targetSource = ((Advised) target).getTargetSource();
            if (log.isDebugEnabled()) {
                log.debug("Target object {} uses cglib proxy");
            }

            try {
                target = targetSource.getTarget();
            } catch (Exception e) {
                throw new ThriftClientInstantiateException("Failed to get target bean from " + target, e);
            }
        }

        Class<?> targetClass = target.getClass();
        final Object targetBean = target;

        ReflectionUtils.doWithFields(targetClass, field -> {
            ThriftReferer thriftReferer = AnnotationUtils.findAnnotation(field, ThriftReferer.class);
            String referName = StringUtils.isNotBlank(thriftReferer.value()) ? thriftReferer.value() : thriftReferer.name();

            Class<?> fieldType = field.getType();
            Object injectedBean;

            if (StringUtils.isNotBlank(referName)) {
                injectedBean = applicationContext.getBean(fieldType, referName);

                injectedBean = Optional.ofNullable(injectedBean)
                        .orElseThrow(() -> new ThriftClientInstantiateException("Detected non-qualified bean with name {}" + referName));

                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, targetBean, injectedBean);
            } else {
                Map<String, ?> injectedBeanMap = applicationContext.getBeansOfType(field.getType());
                if (MapUtils.isEmpty(injectedBeanMap)) {
                    throw new ThriftClientInstantiateException("Detected non-qualified bean of {}" + fieldType.getSimpleName());
                }

                if (injectedBeanMap.size() > 1) {
                    throw new ThriftClientInstantiateException("Detected ambiguous beans of {}" + fieldType.getSimpleName());
                }

                injectedBean = injectedBeanMap.entrySet().stream()
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElseThrow(() -> new ThriftClientInstantiateException(
                                "Detected non-qualified bean of {}" + fieldType.getSimpleName()));

                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, targetBean, injectedBean);
            }

            if (log.isDebugEnabled()) {
                log.debug("Bean {} is injected into target bean {}, field {}", injectedBean, targetBean, field.getName());
            }

        }, field -> (AnnotationUtils.getAnnotation(field, ThriftReferer.class) != null));

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
