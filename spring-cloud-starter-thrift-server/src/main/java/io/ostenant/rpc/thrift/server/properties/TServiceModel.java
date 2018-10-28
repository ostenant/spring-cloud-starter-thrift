package io.ostenant.rpc.thrift.server.properties;

public final class TServiceModel {

    /**
     * 服务模型 - 单线程阻塞式
     */
    public static final String SERVICE_MODEL_SIMPLE = "simple";

    /**
     * 服务模型 - 单线程非阻塞式
     */
    public static final String SERVICE_MODEL_NON_BLOCKING = "nonBlocking";

    /**
     * 服务模型 - 线程池
     */
    public static final String SERVICE_MODEL_THREAD_POOL = "threadPool";

    /**
     * 服务模型 - 半同步半异步
     */
    public static final String SERVICE_MODEL_HS_HA = "hsHa";

    /**
     * 服务模型 - 线程池选择器
     */
    public static final String SERVICE_MODEL_THREADED_SELECTOR = "threadedSelector";

    /**
     * 默认的服务模型
     */
    public static final String SERVICE_MODEL_DEFAULT = SERVICE_MODEL_HS_HA;

}
