package com.icekredit.rpc.thrift.client.scanner;

import com.icekredit.rpc.thrift.client.common.ThriftServiceSignature;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ThriftClientInvocationHandler implements InvocationHandler {

    private ThriftServiceSignature serviceSignature;

    private Class<?> clientClass;

    private Constructor<? extends TServiceClient> clientConstructor;

    private ProxyFactory proxyFactory;

    ThriftClientInvocationHandler(ThriftServiceSignature serviceSignature,
                                  Class<?> clientClass,
                                  Constructor<? extends TServiceClient> clientConstructor) throws Exception {
        this.serviceSignature = serviceSignature;
        this.clientClass = clientClass;
        this.clientConstructor = clientConstructor;
        this.proxyFactory = initializeProxyFactory();
    }


    @SuppressWarnings("unchecked")
    private ProxyFactory initializeProxyFactory() throws Exception {
        Constructor<?> constructor = clientConstructor;
        if (Objects.isNull(constructor)) {
            constructor = clientClass.getConstructor(TProtocol.class);
        }

        Object target = BeanUtils.instantiateClass(constructor, (TProtocol) null);

        ProxyFactory proxyFactory = new ProxyFactory(target);

        ThriftClientAdvice clientAdvice = new ThriftClientAdvice(serviceSignature, clientConstructor);
        proxyFactory.addAdvice(clientAdvice);

        proxyFactory.setProxyTargetClass(true);
        proxyFactory.setFrozen(true);

        return proxyFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return proxyFactory.getProxy();
    }

}
