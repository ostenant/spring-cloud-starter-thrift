package io.ostenant.rpc.thrift.client;

import io.ostenant.rpc.thrift.client.annotation.ThriftRefer;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.client.exception.ThriftClientInstantiateException;
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

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class ThriftClientBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientBeanPostProcessor.class);

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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Target object {} uses jdk dynamic proxy");
            }

            try {
                target = targetSource.getTarget();
            } catch (Exception e) {
                throw new ThriftClientInstantiateException("Failed to get target bean from " + target, e);
            }
        }

        if (AopUtils.isCglibProxy(target)) {
            TargetSource targetSource = ((Advised) target).getTargetSource();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Target object {} uses cglib proxy");
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
            ThriftRefer thriftRefer = AnnotationUtils.findAnnotation(field, ThriftRefer.class);
            String referName = StringUtils.isNotBlank(thriftRefer.value()) ? thriftRefer.value() : thriftRefer.name();

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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Bean {} is injected into target bean {}, field {}", injectedBean, targetBean, field.getName());
            }

        }, field -> (AnnotationUtils.getAnnotation(field, ThriftRefer.class) != null));

        ReflectionUtils.MethodFilter methodFilter = method -> {
            boolean basicCondition = AnnotationUtils.getAnnotation(method, ThriftRefer.class) != null
                    && method.getParameterCount() > 0
                    && method.getReturnType() == Void.TYPE;

            if (!basicCondition) {
                return false;
            }

            return Arrays.stream(method.getParameters())
                    .map(Parameter::getType)
                    .map(ThriftClientAware.class::isAssignableFrom)
                    .reduce((param1, param2) -> param1 && param2)
                    .get();
        };

        ReflectionUtils.doWithMethods(targetClass, method -> {
            Parameter[] parameters = method.getParameters();
            Object objectArray = Arrays.stream(parameters).map(parameter -> {
                Class<?> parameterType = parameter.getType();
                Map<String, ?> injectedBeanMap = applicationContext.getBeansOfType(parameterType);
                if (MapUtils.isEmpty(injectedBeanMap)) {
                    throw new ThriftClientInstantiateException("Detected non-qualified bean of {}" + parameterType.getSimpleName());
                }

                if (injectedBeanMap.size() > 1) {
                    throw new ThriftClientInstantiateException("Detected ambiguous beans of {}" + parameterType.getSimpleName());
                }

                return injectedBeanMap.entrySet().stream()
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElseThrow(() -> new ThriftClientInstantiateException(
                                "Detected non-qualified bean of {}" + parameterType.getSimpleName()));
            }).toArray();

            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, targetBean, objectArray);
        }, methodFilter);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
