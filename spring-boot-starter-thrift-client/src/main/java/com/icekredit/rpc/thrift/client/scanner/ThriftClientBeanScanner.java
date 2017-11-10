package com.icekredit.rpc.thrift.client.scanner;

import com.icekredit.rpc.thrift.client.annotation.ThriftClient;
import com.icekredit.rpc.thrift.client.common.ThriftClientDefinitionProperty;
import com.icekredit.rpc.thrift.client.common.ThriftServiceSignature;
import com.icekredit.rpc.thrift.client.exception.ThriftClientConfigException;
import com.icekredit.rpc.thrift.client.exception.ThriftClientInstantiateException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Set;

public final class ThriftClientBeanScanner extends ClassPathBeanDefinitionScanner {

    private Logger log = LoggerFactory.getLogger(getClass());

    public ThriftClientBeanScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);
        log.info("Packages scanned by thriftClientBeanDefinitionScanner is [{}]",
                StringUtils.join(basePackages, ", "));

        for (BeanDefinitionHolder definitionHolder : definitionHolders) {
            GenericBeanDefinition definition = (GenericBeanDefinition) definitionHolder.getBeanDefinition();
            MutablePropertyValues definitionProperty = definition.getPropertyValues();

            Class<?> beanClass = definition.getBeanClass();
            log.info("Scanned and found thrift client, bean {} assigned from {}",
                    definitionHolder.getBeanName(),
                    definition.getBeanClassName());

            ThriftClient thriftClient = AnnotationUtils.findAnnotation(beanClass, ThriftClient.class);

            String beanName = StringUtils.isBlank(thriftClient.value()) ? thriftClient.name() : thriftClient.value();

            Class<?> referClass = thriftClient.refer();
            ThriftServiceSignature serviceSignature = new ThriftServiceSignature(thriftClient.serviceId(),
                    thriftClient.refer(),
                    thriftClient.version());

            Class<? extends TServiceClient> clientClass = getClientClassFromAnnotation(referClass);
            Constructor<? extends TServiceClient> constructor;

            try {
                constructor = clientClass.getConstructor(TProtocol.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new ThriftClientInstantiateException("Failed to get constructor with args TProtocol", e);
            }

            definitionProperty.addPropertyValue(ThriftClientDefinitionProperty.BEAN_NAME, beanName);
            definitionProperty.addPropertyValue(ThriftClientDefinitionProperty.BEAN_CLASS, beanClass);
            definitionProperty.addPropertyValue(ThriftClientDefinitionProperty.BEAN_CLASS_NAME, beanClass.getName());

            definitionProperty.addPropertyValue(ThriftClientDefinitionProperty.SERVICE_SIGNATURE, serviceSignature);
            definitionProperty.addPropertyValue(ThriftClientDefinitionProperty.CLIENT_CLASS, referClass);
            definitionProperty.addPropertyValue(ThriftClientDefinitionProperty.CLIENT_CONSTRUCTOR, constructor);
        }

        return definitionHolders;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends TServiceClient> getClientClassFromAnnotation(Class<?> referClass) {
        ParameterizedType clientAwareType = (ParameterizedType) referClass.getGenericInterfaces()[0];
        if (Objects.isNull(clientAwareType)) {
            throw new ThriftClientConfigException("Interface annotated with @ThriftClient should be inherited from ThriftClientAware");
        }

        Class<?>[] typeArguments = (Class<?>[]) clientAwareType.getActualTypeArguments();
        if (ArrayUtils.isEmpty(typeArguments) || typeArguments.length == 0) {
            throw new ThriftClientConfigException("ThriftClientAware should declare an argument");
        }

        if (!ClassUtils.isAssignable(typeArguments[0], TServiceClient.class)) {
            throw new ThriftClientConfigException("ThriftClientAware without argument assigned from TServiceClient");
        }

        return (Class<? extends TServiceClient>) typeArguments[0];
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();

        return metadata.hasAnnotation(ThriftClient.class.getName())
                && metadata.isInterface();
    }


}
