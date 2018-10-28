package io.ostenant.rpc.thrift.client.common;

public final class ThriftClientDefinitionProperty {

    /**
     * Thrift Service的服务签名
     */
    public final static String SERVICE_SIGNATURE = "serviceSignature";

    /**
     * Thrift Client的类型
     */
    public final static String CLIENT_CLASS = "clientClass";

    /**
     * Thrift Client的TProcessor带参数构造器
     */
    public final static String CLIENT_CONSTRUCTOR = "clientConstructor";

    /**
     * ThriftClientAware实现类的bean名称
     */
    public final static String BEAN_NAME = "beanName";

    /**
     * ThriftClientAware实现类的类型
     */
    public final static String BEAN_CLASS = "beanClass";

    /**
     * ThriftClientAware实现类的类型名称
     */
    public final static String BEAN_CLASS_NAME = "beanClassName";

    /**
     * Thrift Service类名称
     */
    public static final String SERVICE_CLASS = "serviceClass";

}
