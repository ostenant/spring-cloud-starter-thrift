package io.ostenant.rpc.thrift.client.scanner;


import com.orbitz.consul.Consul;
import io.ostenant.rpc.thrift.client.cache.ThriftServiceMethodCacheManager;
import io.ostenant.rpc.thrift.client.common.ThriftClientContext;
import io.ostenant.rpc.thrift.client.common.ThriftServiceSignature;
import io.ostenant.rpc.thrift.client.discovery.ThriftConsulServerNode;
import io.ostenant.rpc.thrift.client.discovery.ThriftConsulServerNodeList;
import io.ostenant.rpc.thrift.client.exception.*;
import io.ostenant.rpc.thrift.client.loadbalancer.IRule;
import io.ostenant.rpc.thrift.client.loadbalancer.RoundRobinRule;
import io.ostenant.rpc.thrift.client.loadbalancer.ThriftConsulServerListLoadBalancer;
import io.ostenant.rpc.thrift.client.pool.TransportKeyedObjectPool;
import io.ostenant.rpc.thrift.client.properties.ThriftClientPoolProperties;
import io.ostenant.rpc.thrift.client.properties.ThriftClientProperties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
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
import java.util.Objects;

public class ThriftClientAdvice implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientAdvice.class);
    private static final String DISCOVERY_ADDRESS = "http://%s";

    private ThriftServiceSignature serviceSignature;

    private Constructor<? extends TServiceClient> clientConstructor;

    private ThriftConsulServerListLoadBalancer loadBalancer;

    private ThriftClientProperties properties;

    private TransportKeyedObjectPool objectPool;

    public ThriftClientAdvice(ThriftServiceSignature serviceSignature,
                              Constructor<? extends TServiceClient> clientConstructor) {
        this.serviceSignature = serviceSignature;
        this.clientConstructor = clientConstructor;

        String consulAddress = ThriftClientContext.context().getRegistryAddress();
        Consul consul;
        try {
            consul = Consul.builder().withUrl(String.format(DISCOVERY_ADDRESS, consulAddress)).build();
        } catch (Exception e) {
            throw new ThriftClientRegistryException("Unable to access consul server, address is: " + consulAddress, e);
        }

        if (Objects.isNull(consul)) {
            throw new ThriftClientRegistryException("Unable to access consul server, address is: " + consulAddress);
        }

        ThriftConsulServerNodeList serverNodeList = ThriftConsulServerNodeList.singleton(consul);

        IRule routerRule = new RoundRobinRule();
        this.loadBalancer = new ThriftConsulServerListLoadBalancer(serverNodeList, routerRule);

        routerRule.setLoadBalancer(loadBalancer);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (Objects.isNull(properties)) {
            this.properties = ThriftClientContext.context().getProperties();
        }

        if (Objects.isNull(objectPool)) {
            this.objectPool = ThriftClientContext.context().getObjectPool();
        }

        ThriftClientPoolProperties poolProperties = properties.getPool();

        String serviceId = serviceSignature.getThriftServiceId();
        ThriftConsulServerNode serverNode = loadBalancer.chooseServerNode(serviceId);
        String signature = serviceSignature.marker();

        Method invocationMethod = invocation.getMethod();
        Object[] args = invocation.getArguments();
        int retryTimes = 0;

        TTransport transport = null;

        while (true) {
            if (retryTimes++ > poolProperties.getRetryTimes()) {
                LOGGER.error(
                        "All thrift client call failed, method is {}, args is {}, retryTimes: {}",
                        invocation.getMethod().getName(), args, retryTimes);
                throw new ThriftClientException("Thrift client call failed, thrift client signature is: " + serviceSignature.marker());
            }

            try {
                transport = objectPool.borrowObject(serverNode);

                TProtocol protocol = new TCompactProtocol(transport);
                TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol,
                        signature);

                Object client = clientConstructor.newInstance(multiplexedProtocol);

                Method cachedMethod = ThriftServiceMethodCacheManager.getMethod(client.getClass(),
                        invocationMethod.getName(),
                        invocationMethod.getParameterTypes());

                return ReflectionUtils.invokeMethod(cachedMethod, client, args);

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

                        LOGGER.error("Thrift client request timeout, ip is {}, port is {}, timeout is {}, method is {}, args is {}",
                                serverNode.getHost(), serverNode.getPort(), serverNode.getTimeout(),
                                invocation.getMethod(), args);
                        throw new ThriftClientRequestTimeoutException("Thrift client request timeout", e);

                    } else if (realException == null && innerException.getType() == TTransportException.END_OF_FILE) {
                        // 服务端直接抛出了异常 or 服务端在被调用的过程中被关闭了
                        objectPool.clear(serverNode); // 把以前的对象池进行销毁
                        if (transport != null) {
                            transport.close();
                        }

                    } else if (realException instanceof SocketException) {
                        objectPool.clear(serverNode);
                        if (transport != null) {
                            transport.close();
                        }
                    }

                } else if (undeclaredThrowable instanceof TApplicationException) {  // 有可能服务端返回的结果里存在null
                    LOGGER.error(
                            "Thrift end of file, ip is {}, port is {}, timeout is {}, method is {}, args is {}, retryTimes is {}",
                            serverNode.getHost(), serverNode.getPort(), serverNode.getTimeout(),
                            invocation.getMethod(), args, retryTimes);
                    if (retryTimes >= poolProperties.getRetryTimes()) {
                        throw new ThriftApplicationException("Thrift end of file", e);
                    }

                    objectPool.clear(serverNode);
                    if (transport != null) {
                        transport.close();
                    }

                } else if (undeclaredThrowable instanceof TException) { // idl exception
                    throw undeclaredThrowable;
                } else {
                    // Unknown Exception
                    throw e;
                }

            } catch (Exception e) {
                if (e instanceof ThriftClientOpenException) { // 创建连接失败
                    Throwable realCause = e.getCause().getCause();
                    // unreachable, reset router
                    if (realCause instanceof SocketException && realCause.getMessage().contains("Network is unreachable")) {
                        throw e;
                    }
                } else {
                    throw e;
                }
            } finally {
                try {
                    if (objectPool != null && transport != null) {
                        objectPool.returnObject(serverNode, transport);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}
