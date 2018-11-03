package io.ostenant.rpc.thrift.client.scanner;

import io.ostenant.rpc.thrift.client.common.ThriftServiceSignature;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ThriftClientInvocationHandler implements InvocationHandler {

    private ThriftServiceSignature serviceSignature;

    private Class<?> clientClass;

    private Constructor<? extends TServiceClient> clientConstructor;

    private ProxyFactoryBean proxyFactoryBean;

    public ThriftClientInvocationHandler(ThriftServiceSignature serviceSignature,
                                         Class<?> clientClass,
                                         Constructor<? extends TServiceClient> clientConstructor) throws Exception {
        this.serviceSignature = serviceSignature;
        this.clientClass = clientClass;
        this.clientConstructor = clientConstructor;
        this.proxyFactoryBean = initializeProxyFactoryBean();
    }

    @SuppressWarnings("unchecked")
    private ProxyFactoryBean initializeProxyFactoryBean() throws Exception {
        Constructor<?> constructor = clientConstructor;
        if (Objects.isNull(constructor)) {
            constructor = clientClass.getConstructor(TProtocol.class);
        }

        Object target = BeanUtils.instantiateClass(constructor, (TProtocol) null);

        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(target);
        factoryBean.setBeanClassLoader(getClass().getClassLoader());

        ThriftClientAdvice clientAdvice = new ThriftClientAdvice(serviceSignature, clientConstructor);
        factoryBean.addAdvice(clientAdvice);

        factoryBean.setProxyTargetClass(true);
        factoryBean.setSingleton(true);
        factoryBean.setOptimize(true);
        factoryBean.setFrozen(true);
        return factoryBean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return proxyFactoryBean.getObject();
    }
}
