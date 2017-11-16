package com.icekredit.rpc.thrift.client.scanner;


import com.icekredit.rpc.thrift.client.common.ThriftClientContext;
import com.icekredit.rpc.thrift.client.common.ThriftServerNode;
import com.icekredit.rpc.thrift.client.common.ThriftServiceSignature;
import com.icekredit.rpc.thrift.client.exception.ThriftApplicationException;
import com.icekredit.rpc.thrift.client.exception.ThriftClientException;
import com.icekredit.rpc.thrift.client.exception.ThriftClientOpenException;
import com.icekredit.rpc.thrift.client.exception.ThriftClientRequestTimeoutException;
import com.icekredit.rpc.thrift.client.pool.TransportKeyedObjectPool;
import com.icekredit.rpc.thrift.client.properties.ThriftClientPoolProperties;
import com.icekredit.rpc.thrift.client.properties.ThriftClientProperties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ThriftClientAdvice implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ThriftServiceSignature serviceSignature;

    private Constructor<? extends TServiceClient> clientConstructor;

    private ThriftClientProperties properties;

    private TransportKeyedObjectPool objectPool;


    ThriftClientAdvice(ThriftServiceSignature serviceSignature,
                              Constructor<? extends TServiceClient> clientConstructor) {
        this.serviceSignature = serviceSignature;
        this.clientConstructor = clientConstructor;

        ThriftClientContext clientContext = ThriftClientContext.getContext();
        this.properties = clientContext.getProperties();
        this.objectPool = clientContext.getObjectPool();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ThriftClientPoolProperties poolProperties = properties.getPool();

        ThriftServerNode node = new ThriftServerNode("localhost", 25000);

        String signature = serviceSignature.marker();

        Object[] args = invocation.getArguments();

        int retryTimes = 0;

        while (true) {
            if (++retryTimes == poolProperties.getRetryTimes()) {
                log.error(
                        "All thrift client call failed, method is {}, args is {}, retryTimes: {}",
                        invocation.getMethod().getName(), args, retryTimes);
                throw new ThriftClientException("Thrift client call failed, thrift client signature is " + serviceSignature);
            }

            TTransport transport = null;
            try {
                transport = objectPool.borrowObject(node);
                TProtocol protocol = new TBinaryProtocol(transport);
                TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol,
                        signature);
                Object client = clientConstructor.newInstance(multiplexedProtocol);

                Method invocationMethod = invocation.getMethod();

                return ReflectionUtils.invokeMethod(invocationMethod, client, args);

            } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | SecurityException | NoSuchMethodException e) {
                throw new ThriftClientOpenException("Unable to open thrift client", e);

            } catch (UndeclaredThrowableException e) {

                Throwable undeclaredThrowable = e.getUndeclaredThrowable();
                if (undeclaredThrowable instanceof TTransportException) {
                    TTransportException innerException = (TTransportException) e.getUndeclaredThrowable();
                    Throwable realException = innerException.getCause();

                    if (realException instanceof SocketTimeoutException) { // 超时,直接抛出异常,不进行重试
                        if (transport != null) {
                            transport.close();
                        }

                        log.error("Thrift client request timeout, ip is {}, port is {}, timeout is {}, method is {}, args is {}",
                                node.getHost(), node.getPort(), node.getTimeout(),
                                invocation.getMethod(), args);
                        throw new ThriftClientRequestTimeoutException("Thrift client request timeout", e);

                    } else if (realException == null && innerException.getType() == TTransportException.END_OF_FILE) {
                        // 服务端直接抛出了异常 or 服务端在被调用的过程中被关闭了
                        objectPool.clear(node); // 把以前的对象池进行销毁
                        if (transport != null) {
                            transport.close();
                        }

                    } else if (realException instanceof SocketException) {
                        objectPool.clear(node);
                        if (transport != null) {
                            transport.close();
                        }

                    }
                } else if (undeclaredThrowable instanceof TApplicationException) {  // 有可能服务端返回的结果里存在null
                    log.error(
                            "Thrift end of file, ip is {}, port is {}, timeout is {}, method is {}, args is {}",
                            node.getHost(), node.getPort(), node.getTimeout(),
                            invocation.getMethod(), args);
                    throw new ThriftApplicationException("Thrift end of file", e);

                } else if (undeclaredThrowable instanceof TException) { // idl exception
                    throw undeclaredThrowable;
                }
                // Unknown Exception
                throw e;

            } catch (Exception e) {
                if (e instanceof ThriftClientOpenException) { // 创建连接失败
                    Throwable realCause = e.getCause().getCause();
                    // unreachable, reset router
                    if (realCause instanceof SocketException && realCause.getMessage().contains("Network is unreachable")) {
                        throw e;
                    }
                }
            } finally {
                try {
                    if (objectPool != null && transport != null) {
                        objectPool.returnObject(node, transport);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }
}
