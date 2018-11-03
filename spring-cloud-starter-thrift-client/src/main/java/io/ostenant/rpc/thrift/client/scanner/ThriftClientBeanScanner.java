package io.ostenant.rpc.thrift.client.scanner;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientDefinitionProperty;
import io.ostenant.rpc.thrift.client.common.ThriftServiceSignature;
import io.ostenant.rpc.thrift.client.exception.ThriftClientConfigException;
import io.ostenant.rpc.thrift.client.exception.ThriftClientInstantiateException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;

public final class ThriftClientBeanScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientBeanScanner.class);

    public ThriftClientBeanScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(ThriftClient.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);
        LOGGER.info("Packages scanned by thriftClientBeanDefinitionScanner is [{}]",
                StringUtils.join(basePackages, ", "));

        for (BeanDefinitionHolder definitionHolder : definitionHolders) {
            GenericBeanDefinition definition = (GenericBeanDefinition) definitionHolder.getBeanDefinition();

            LOGGER.info("Scanned and found thrift client, bean {} assigned from {}",
                    definitionHolder.getBeanName(),
                    definition.getBeanClassName());

            Class<?> beanClass;
            try {
                beanClass = Class.forName(definition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            ThriftClient thriftClient = AnnotationUtils.findAnnotation(beanClass, ThriftClient.class);
            if (thriftClient == null) {
                LOGGER.warn("Thrift client is not found");
                continue;
            }

            String beanName = StringUtils.isNotBlank(thriftClient.value())
                    ? thriftClient.value()
                    : (StringUtils.isNotBlank(thriftClient.name()) ? thriftClient.name() : StringUtils.uncapitalize(beanClass.getSimpleName()));

            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.BEAN_NAME, beanName);
            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.BEAN_CLASS, beanClass);
            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.BEAN_CLASS_NAME, beanClass.getName());

            Class<?> referClass = thriftClient.refer();
            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.SERVICE_CLASS, referClass);

            ThriftServiceSignature serviceSignature = new ThriftServiceSignature(
                    thriftClient.serviceId(), thriftClient.refer(), thriftClient.version());

            Class<? extends TServiceClient> clientClass = getClientClassFromAnnotation(beanClass);
            Constructor<? extends TServiceClient> constructor;

            try {
                constructor = clientClass.getConstructor(TProtocol.class);
            } catch (NoSuchMethodException e) {
                LOGGER.error(e.getMessage(), e);
                throw new ThriftClientInstantiateException("Failed to get constructor with args TProtocol", e);
            }

            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.SERVICE_SIGNATURE, serviceSignature);
            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.CLIENT_CLASS, clientClass);
            definition.getPropertyValues().addPropertyValue(ThriftClientDefinitionProperty.CLIENT_CONSTRUCTOR, constructor);
            definition.setBeanClass(ThriftClientFactoryBean.class);
        }

        return definitionHolders;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends TServiceClient> getClientClassFromAnnotation(Class<?> beanClass) {
        ParameterizedType clientAwareType = (ParameterizedType) beanClass.getGenericInterfaces()[0];
        if (Objects.isNull(clientAwareType)) {
            throw new ThriftClientConfigException("Interface annotated with @ThriftClient should be inherited from ThriftClientAware");
        }

        Type[] typeArguments = clientAwareType.getActualTypeArguments();
        if (ArrayUtils.isEmpty(typeArguments) || typeArguments.length == 0) {
            throw new ThriftClientConfigException("ThriftClientAware should declare an argument");
        }

        Class<?> clientClass = (Class<?>) typeArguments[0];
        if (!ClassUtils.isAssignable(TServiceClient.class, clientClass)) {
            throw new ThriftClientConfigException("ThriftClientAware without argument inherited from TServiceClient");
        }

        return (Class<? extends TServiceClient>) clientClass;
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.hasAnnotation(ThriftClient.class.getName())
                && metadata.isInterface();
    }
}
