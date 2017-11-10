package com.icekredit.rpc.thrift.client.scanner;

import com.icekredit.rpc.thrift.client.common.ThriftClientAware;
import com.icekredit.rpc.thrift.client.common.ThriftServiceSignature;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

public class ThriftClientFactoryBean<T extends ThriftClientAware> implements FactoryBean<T>, InitializingBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    private String beanName;

    private Class<?> beanClass;

    private String beanClassName;

    private ThriftServiceSignature serviceSignature;

    private Class<?> clientClass;

    private Constructor<? extends TServiceClient> clientConstructor;

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        if (Objects.isNull(beanClass)) {
            beanClass = Class.forName(beanName);
        }

        if (beanClass.isInterface()) {
            log.info("Ready to generate proxy with JDK");

            return (T) Proxy.newProxyInstance(
                    beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                        Object result = method.invoke(proxy, args);
                        return result;
                    }
            );
        } else {
            log.info("Ready to generate proxy with Cglib");

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
            enhancer.setUseFactory(true);

            MethodInterceptor callback = (target, method, args, methodProxy) -> {
                Object result = methodProxy.invokeSuper(target, args);
                return result;
            };

            enhancer.setCallback(callback);
            return (T) enhancer.create();
        }

    }

    @Override
    public Class<?> getObjectType() {
        if (Objects.isNull(beanClass) && StringUtils.isBlank(beanName)) {
            log.info("Bean class is not found");
            return null;
        }

        if (Objects.isNull(beanClass)) {
            log.info("Bean class is: {}", beanClass.getName());
            return beanClass;
        }

        if (StringUtils.isNotBlank(beanClassName)) {
            try {
                beanClass = Class.forName(beanClassName);
                log.info("Bean class is: {}", beanClassName);
                return beanClass;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        log.info("Bean class is not found");
        return null;
    }

    @Override
    public boolean isSingleton() {
        TypeVariable<?> typeVariable = this.getClass().getTypeParameters()[0];
        log.info("ThriftClientFactoryBean<{}> is in singleton pattern", typeVariable.getName());
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Success to Instantiate a bean of ThriftClientFactoryBean<{}>");
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public ThriftServiceSignature getServiceSignature() {
        return serviceSignature;
    }

    public void setServiceSignature(ThriftServiceSignature serviceSignature) {
        this.serviceSignature = serviceSignature;
    }

    public Class<?> getClientClass() {
        return clientClass;
    }

    public void setClientClass(Class<?> clientClass) {
        this.clientClass = clientClass;
    }

    public Constructor<? extends TServiceClient> getClientConstructor() {
        return clientConstructor;
    }

    public void setClientConstructor(Constructor<? extends TServiceClient> clientConstructor) {
        this.clientConstructor = clientConstructor;
    }
}
