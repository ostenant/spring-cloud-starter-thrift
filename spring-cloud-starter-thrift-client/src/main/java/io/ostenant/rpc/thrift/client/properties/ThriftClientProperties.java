package io.ostenant.rpc.thrift.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.thrift.client")
public class ThriftClientProperties {

    /**
     * 服务模型(单线程/多线程/阻塞/非阻塞)
     * <p>
     * simple: 单线程阻塞模型
     * nonBlocking: 单线程非阻塞模型
     * threadPool: 线程池同步模型
     * hsHa: 半同步半异步模型
     * threadedSelector: 线程池选择器模型
     * </p>
     */
    private String serviceModel = TServiceModel.SERVICE_MODEL_DEFAULT;

    private String packageToScan = DEFAULT_PACKAGE_TO_SCAN;

    private ThriftClientPoolProperties pool;

    /**
     * 客户端扫描的包名称/多个子包用逗号分割
     */
    private final static String DEFAULT_PACKAGE_TO_SCAN = "";

    public String getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(String serviceModel) {
        this.serviceModel = serviceModel;
    }

    public String getPackageToScan() {
        return packageToScan;
    }

    public void setPackageToScan(String packageToScan) {
        this.packageToScan = packageToScan;
    }

    public ThriftClientPoolProperties getPool() {
        return pool;
    }

    public void setPool(ThriftClientPoolProperties pool) {
        this.pool = pool;
    }
}
