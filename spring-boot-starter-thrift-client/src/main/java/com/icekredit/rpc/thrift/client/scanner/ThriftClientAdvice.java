package com.icekredit.rpc.thrift.client.scanner;


import com.icekredit.rpc.thrift.client.common.ThriftServiceSignature;
import com.icekredit.rpc.thrift.client.exception.ThriftClientOpenException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ThriftClientAdvice implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ThriftServiceSignature serviceSignature;

    private Constructor<? extends TServiceClient> clientConstructor;


    public ThriftClientAdvice(ThriftServiceSignature serviceSignature,
                              Constructor<? extends TServiceClient> clientConstructor) {
        this.serviceSignature = serviceSignature;
        this.clientConstructor = clientConstructor;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String signature = serviceSignature.marker();

        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket("localhost", 25000, 30000));
            TProtocol protocol = new TBinaryProtocol(transport);

            TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol,
                    signature);

            Object client = clientConstructor.newInstance(multiplexedProtocol);

            transport.open();

            Method invocationMethod = invocation.getMethod();
            Object[] args = invocation.getArguments();

            return ReflectionUtils.invokeMethod(invocationMethod, client, args);

        } catch (Exception e) {
            throw new ThriftClientOpenException("Unable to open thrift client", e);
        } finally {
            if (transport != null) {
                transport.close();
            }
        }

    }
}
