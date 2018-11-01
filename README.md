# spring-cloud-starter-thrift

## 简介

`spring-cloud-starter-thrift`提供`Spring Cloud`对可伸缩的跨语言服务调用框架`Apache Thrift`的封装和集成。

`spring-cloud-starter-thrift`包括客户端`spring-cloud-starter-thrift-client`和服务端`spring-cloud-starter-thrift-server`两个模块。

**服务端：**
  1. 支持`Apache Thrift`的各种原生服务线程模型，包括单线程阻塞模型(`simple`)、单线程非阻塞模型(`nonBlocking`)、线程池阻塞模型(`threadPool`)、半同步半异步模型(`hsHa`)和半同步半异步线程选择器模型(`threadedSelector`)。
  2. 支持`Apache Thrift 0.10.0`版本后提供的多路复用处理器，提供服务的统一注册管理功能。
  3. 支持由服务签名(服务`ID` + 客户端`Stub`接口名称 + 服务版本号)唯一标识服务`Stub`的具体实现类，支持服务版本的平滑升级。
  4. 支持`Server Group`形式的启动方式，每个服务实例可以开启多台`Thrift Server`，通过不同的端口号暴露给客户端。

**客户端：**
  1. 支持由服务签名(服务`ID` + 客户端`Stub`接口名称 + 服务版本号)唯一标识和调用服务端的`Stub`具体实现类。
  2. 支持`Apache Thrift`的`Transport`层的连接池管理，减少了客户端与服务端之间连接的频繁创建和销毁。
  3. 支持与`Spring Cloud Consul`的无缝集成，客户端通过心跳检测与服务注册中心`Consul`保持连接，动态定时的刷新服务列表、监测服务的启用、关闭和健康状态。
  4. 支持客户端负载均衡，包括随机、轮询的负载均衡策略，客户端的`Thrift`程序通过本地的服务缓存列表实现调用的动态转发。
  

## 快速开始

`spring-cloud-starter-thrift`使用的是`0.10.0`版本的`thrift`。

首先，通过`Apache Thrift`的`IDL`(接口描述语言)编写客户端桩`Stub`和服务端骨架`Skeleton`，通过`.thrift`文件定义接口规范。

calculator.thrift

```thrift
namespace java io.ostenant.rpc.thrift.examples

service CalculatorService {
    i32 add(1: i32 arg1, 2: i32 arg2)
    i32 subtract(1: i32 arg1, 2: i32 arg2)
    i32 multiply(1: i32 arg1, 2: i32 arg2)
    i32 division(1: i32 arg1, 2: i32 arg2)
}

```

下载并安装`0.10.0`的`Thrift IDL`编译器，下载地址[http://thrift.apache.org/docs/install](http://thrift.apache.org/docs/install)。通过编译器生成`.java`接口的类文件。

```bash
thrift-0.10.0.exe -r -gen ./CalculatorService.thrift
```

编译器生成`CalculatorService.java`文件。`CalculatorService.java`有成千上万行代码。
对于开发人员而言，使用原生的`Thrift`框架，仅仅需要关注以下四个核心接口/类：Iface, AsyncIface, Client和AsyncClient。

* Iface：服务端通过实现CalculatorService.Iface接口，向客户端的提供具体的同步业务逻辑。
* AsyncIface：服务端通过实现CalculatorService.Iface接口，向客户端的提供具体的异步业务逻辑。
* Client：客户端通过CalculatorService.Client的实例对象，以同步的方式访问服务端提供的服务方法。
* AsyncClient：客户端通过CalculatorService.AsyncClient的实例对象，以异步的方式访问服务端提供的服务方法。

CalculatorService.java

```java
public class CalculatorService {

  public interface Iface {

    public int add(int arg1, int arg2) throws org.apache.thrift.TException;

    public int subtract(int arg1, int arg2) throws org.apache.thrift.TException;

    public int multiply(int arg1, int arg2) throws org.apache.thrift.TException;

    public int division(int arg1, int arg2) throws org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void add(int arg1, int arg2, org.apache.thrift.async.AsyncMethodCallback<Integer> resultHandler) throws org.apache.thrift.TException;

    public void subtract(int arg1, int arg2, org.apache.thrift.async.AsyncMethodCallback<Integer> resultHandler) throws org.apache.thrift.TException;

    public void multiply(int arg1, int arg2, org.apache.thrift.async.AsyncMethodCallback<Integer> resultHandler) throws org.apache.thrift.TException;

    public void division(int arg1, int arg2, org.apache.thrift.async.AsyncMethodCallback<Integer> resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client extends org.apache.thrift.TServiceClient implements Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      super(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
      super(iprot, oprot);
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

  }
}

// ... 省略

```

### 服务端程序：

在`pom.xml`文件中引入服务端依赖`spring-cloud-starter-thrift-server`：

```xml
    <dependency>
        <groupId>io.ostenant.rpc.thrift</groupId>
        <artifactId>spring-cloud-starter-thrift-server</artifactId>
        <version>1.0.0</version>
    </dependency>
```

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>io.ostenant.rpc.thrift</groupId>
        <artifactId>spring-cloud-starter-thrift-example</artifactId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-cloud-starter-thrift-example-server</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>io.ostenant.rpc.thrift</groupId>
            <artifactId>spring-boot-starter-thrift-server</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>io.ostenant.rpc.thrift</groupId>
            <artifactId>spring-cloud-starter-thrift-example-iface</artifactId>
            <version>1.0.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

```

application.yml

```yaml
## 服务所在的HTTP端口号
server:
  port: 8080

## 用于Consul健康检查
endpoints:
  actuator:
    sensitive: false
    enabled: true
management:
  security:
    enabled: false

## Spring Thrift服务端配置
spring:
  thrift:
    server:
      service-id: thrift-rpc-calculator ## 服务ID
      service-model: hsHa ## 服务线程模型simple/nonBlocking/threadPool/hsHa/threadedSelector
      port: 25000 ## 服务所在的RPC端口号
      worker-queue-capacity: 1000 ## 工作队列大小
      hs-ha:
        min-worker-threads: 5
        max-worker-threads: 20
        keep-alived-time: 3
```

Application.java

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

实现`Thrift IDL`生成的`Stub`类`CalculatorService`中的`Iface`接口，编写具体的业务逻辑：

RpcCalculatorService.java

```java
@ThriftService(name = "rpcCalculatorService", version = 2.0)
public class RpcCalculatorService implements CalculatorService.Iface {

    @Override
    public int add(int arg1, int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.add(arg2Decimal).intValue();
    }

    @Override
    public int subtract(int arg1, int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.subtract(arg2Decimal).intValue();
    }

    @Override
    public int multiply(int arg1, int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.multiply(arg2Decimal).intValue();
    }

    @Override
    public int division(int arg1, int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.divide(arg2Decimal).intValue();
    }
}

```

编写`Dockerfile`文件：

```dockerfile
FROM openjdk:8-jdk-alpine
ADD target/spring-boot-thrift-server-1.0.0jar calculator-server.jar
ENTRYPOINT ["java", "-jar", "calculator-server.jar"]
```

将`Dockerfile`和`target/spring-boot-thrift-server-0.0.1-SNAPSHOT.jar`拷贝到服务器上，构建`Thrift Server`的服务镜像：
```docker
docker build . -t ostenant/calculator-server
```

启动三个`Thrift Server`的`docker`容器，分别指定对应的端口号和`Consul`注册信息：

`Thrift Server`实例1(25001端口)：

```docker
docker run -d -p 8081:8080 -p 25001:25000 --name calculator-server-01 \
    -e "SERVICE_25000_NAME=thrift-rpc-calculator" \
	-e "SERVICE_25000_CHECK_TCP=/" \
	-e "SERVICE_25000_CHECK_INTERVAL=30s" \
	-e "SERVICE_25000_CHECK_TIMEOUT=3s" \
	-e "SERVICE_25000_TAGS=thrift-rpc-calculator-25001" \
	 ostenant/calculator-server
```

`Thrift Server`实例2(25002端口)：

```docker
docker run -d -p 8081:8080 -p 25002:25000 --name calculator-server-01 \
    -e "SERVICE_25000_NAME=thrift-rpc-calculator" \
	-e "SERVICE_25000_CHECK_TCP=/" \
	-e "SERVICE_25000_CHECK_INTERVAL=30s" \
	-e "SERVICE_25000_CHECK_TIMEOUT=3s" \
	-e "SERVICE_25000_TAGS=thrift-rpc-calculator-25002" \
	 ostenant/calculator-server
```

`Thrift Server`实例3(25003端口)：

```docker
docker run -d -p 8081:8080 -p 25003:25000 --name calculator-server-01 \
    -e "SERVICE_25000_NAME=thrift-rpc-calculator" \
	-e "SERVICE_25000_CHECK_TCP=/" \
	-e "SERVICE_25000_CHECK_INTERVAL=30s" \
	-e "SERVICE_25000_CHECK_TIMEOUT=3s" \
	-e "SERVICE_25000_TAGS=thrift-rpc-calculator-25003" \
	 ostenant/calculator-server
```

启动`Consul`和`Registrator`容器，`Thrift Server`的三个服务实例成功注册到Consul服务列表：

![](http://ols3fdyll.bkt.clouddn.com/Thrift-Server-Consul.png)

这里有关`Consul`和`Registrator`的安装配置以及使用，请参考官方文档，这里就不加以累述了！

观察各个容器的启动日志，如果包含以下几行输出信息，则表明`Thrift Server`成功启动并正常提供`RPC`服务。

```bash
2017-11-19 22:28:47.779  INFO 12960 --- [           main] c.i.r.t.s.context.ThriftServerContext    : Build thrift server from HsHaServerContext
2017-11-19 22:28:47.820  INFO 12960 --- [           main] c.i.r.t.s.p.TRegisterProcessorFactory    : Processor bean org.ostenant.springboot.learning.examples.CalculatorService$Processor@445bce9a with signature [thrift-rpc-calculator$org.ostenant.springboot.learning.examples.CalculatorService$2.0] is instantiated
2017-11-19 22:28:47.822  INFO 12960 --- [           main] c.i.r.t.s.p.TRegisterProcessorFactory    : Single processor org.ostenant.springboot.learning.examples.CalculatorService$Processor@445bce9a register onto multiplexed processor with signature [thrift-rpc-calculator$org.ostenant.springboot.learning.examples.CalculatorService$2.0]
2017-11-19 22:28:47.822  INFO 12960 --- [           main] c.i.r.t.s.p.TRegisterProcessorFactory    : Multiplexed processor totally owns 1 service processors
```

至此，`Thrift`服务端程序正常运行，可供客户端`Thrift`程序调用！

### 客户端程序：

在`pom.xml`文件中引入客户端依赖`spring-cloud-starter-thrift-client`：

```xml
    <dependency>
        <groupId>io.ostenant.rpc.thrift</groupId>
        <artifactId>spring-cloud-starter-thrift-client</artifactId>
        <version>1.0.0</version>
    </dependency>
```

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>io.ostenant.rpc.thrift</groupId>
        <artifactId>spring-cloud-starter-thrift-example</artifactId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-cloud-starter-thrift-example-client</artifactId>

    <dependencies>
        <dependency>
            <groupId>io.ostenant.rpc.thrift</groupId>
            <artifactId>spring-cloud-starter-thrift-client</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>io.ostenant.rpc.thrift</groupId>
            <artifactId>spring-cloud-starter-thrift-example-iface</artifactId>
            <version>1.0.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

application.yml

```yaml
## 客户端的HTTP端口号
server:
  port: 8080

## 用于Consul健康检查
endpoints:
  actuator:
    sensitive: false
    enabled: true
management:
  security:
    enabled: false

## Spring Thrift客户端配置(Thrift Client的自动配置取决于Spring Cloud Consul的正确配置)
spring:
  cloud:
    consul:
      host: 127.0.0.1 ## Consul的IP地址
      port: 8500 ## Consul的端口号
      discovery:
        register: false ## 是否将自身注册为服务
        register-health-check: true 
        health-check-interval: 30s ## HTTP服务健康检查时间间隔
      retry:
        max-attempts: 3
        max-interval: 2000
  thrift:
    client:
      package-to-scan: io.ostenant.rpc.thrift.example.rpc ## 标记由有注解@ThriftClient接口的包路径
      service-model: hsHa ## 服务线程模型（这里必须与服务端保持一致, 默认都是hsHa）
      pool: ## 客户端连接池配置
        retry-times: 3 
        pool-max-total-per-key: 200
        pool-min-idle-per-key: 10
        pool-max-idle-per-key: 40
        pool-max-wait: 1000
        connect-timeout: 2000

```

Application.java

```java
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

```

编写`Thrift Client`的客户端代理接口，这里有两点注意事项：
  1. 接口需要继承于父接口`ThriftClientAware`，且`ThriftClientAware`里的泛型参数填写为`Thrift IDL`生成的`Stub`类`CalculatorService`中的`Client`内部类。
  > 接口中有一个抽象方法T thriftClient()可以获取具体的`Thrift Client`的代理对象。
  
  2. 接口标识注解@ThriftClient：
  > (a). serviceId：此客户端代理接口绑定的`Thrift`服务端的服务注册ID(与服务端保持一致)。
  > (b). refer：`Thrift`服务端具体的`Stub`生成类。
  > (c). version: `Stub`生成类具体业务实现类的版本号(不填写默认为1.0)，需要与服务端保持一致。
  
  
```java
@ThriftClient(serviceId = "thrift-rpc-calculator", refer = CalculatorService.class, version = 2.0)
public interface CalculatorThriftClient extends ThriftClientAware<CalculatorService.Client> {
}

```

在`Controller`中注入ThriftClientAware<? extend TServiceClient>子接口，使用注解@ThriftReferer标识即可成功注入。
使用时，通过XxxxThriftClient.thriftClient()即可调用远程`Thrift Server`的服务方法。


RpcCalculatorController.java

```java 
@RestController
@RequestMapping("/rpc")
public class RpcCalculatorController {

    @ThriftReferer
    CalculatorThriftClient calculators;

    @GetMapping("/add")
    public int add(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().add(arg1, arg2);
    }

    @GetMapping("/subtract")
    public int subtract(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().subtract(arg1, arg2);

    }

    @GetMapping("/multiply")
    public int multiply(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().multiply(arg1, arg2);
    }

    @GetMapping("/division")
    public int division(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().division(arg1, arg2);
    }
}

```
在本地`8080`端口号启动`Thrift`客户端，正常启动后观察启动日志如下：

```bash
2017-11-20 11:00:20.025  INFO 4052 --- [           main] .r.t.c.ThriftClientBeanScannerConfigurer : Base package org.ostenant.springboot.learning.examples.rpc is to be scanned with com.icekredit.rpc.thrift.client.scanner.ThriftClientBeanScanner@37496720
2017-11-20 11:00:20.029  INFO 4052 --- [           main] c.i.r.t.c.s.ThriftClientBeanScanner      : Packages scanned by thriftClientBeanDefinitionScanner is [org.ostenant.springboot.learning.examples.rpc]
2017-11-20 11:00:20.029  INFO 4052 --- [           main] c.i.r.t.c.s.ThriftClientBeanScanner      : Scanned and found thrift client, bean calculatorThriftClient assigned from org.ostenant.springboot.learning.examples.rpc.CalculatorThriftClient
2017-11-20 11:00:20.050  INFO 4052 --- [           main] f.a.AutowiredAnnotationBeanPostProcessor : JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
2017-11-20 11:00:20.134  INFO 4052 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.ostenant.springboot.learning.examples.rest.CalculatorFeignClient' of type [org.springframework.cloud.netflix.feign.FeignClientFactoryBean] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2017-11-20 11:00:20.136  WARN 4052 --- [           main] c.i.r.t.c.s.ThriftClientFactoryBean      : Bean class is not found
2017-11-20 11:00:20.142  INFO 4052 --- [           main] c.i.r.t.c.s.ThriftClientFactoryBean      : Succeed to instantiate an instance of ThriftClientFactoryBean: com.icekredit.rpc.thrift.client.scanner.ThriftClientFactoryBean@7bac686b
2017-11-20 11:00:20.142  INFO 4052 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'calculatorThriftClient' of type [com.icekredit.rpc.thrift.client.scanner.ThriftClientFactoryBean] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2017-11-20 11:00:20.411  INFO 4052 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.cloud.netflix.metrics.MetricsInterceptorConfiguration$MetricsRestTemplateConfiguration' of type [org.springframework.cloud.netflix.metrics.MetricsInterceptorConfiguration$MetricsRestTemplateConfiguration$$EnhancerBySpringCGLIB$$a9ef18dc] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2017-11-20 11:00:20.423  INFO 4052 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration' of type [org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration$$EnhancerBySpringCGLIB$$93dc7598] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2017-11-20 11:00:21.592  INFO 4052 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 8080 (http)
```

启动过程中，所有的标记有注解@ThriftClient的接口都生成了代理对象，并通过注解@ThriftReferer注入到Controller中。

同时，`客户端`启动时开启了一个`ServerUpdater`，定时动态的去`Consul`服务注册列表抓取健康的服务信息，缓存到本地服务列表中。

```bash
2017-11-20 11:02:26.726  INFO 4052 --- [erListUpdater-0] t.c.l.ThriftConsulServerListLoadBalancer : Refreshed thrift serverList: [thrift-rpc-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25001], host='192.168.91.128', port=25001, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25002], host='192.168.91.128', port=25002, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25003], host='192.168.91.128', port=25003, address='192.168.91.128', isHealth=true}], consul-8301: [ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=8301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=9301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=10301, address='192.168.91.128', isHealth=true}], consul-8302: [ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=8302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=9302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=10302, address='192.168.91.128', isHealth=true}], thrift-rest-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8081], host='192.168.91.128', port=8081, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8082], host='192.168.91.128', port=8082, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8083], host='192.168.91.128', port=8083, address='192.168.91.128', isHealth=true}]]
2017-11-20 11:02:56.752  INFO 4052 --- [erListUpdater-0] t.c.l.ThriftConsulServerListLoadBalancer : Refreshed thrift serverList: [thrift-rpc-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25001], host='192.168.91.128', port=25001, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25002], host='192.168.91.128', port=25002, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25003], host='192.168.91.128', port=25003, address='192.168.91.128', isHealth=true}], consul-8301: [ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=8301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=9301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=10301, address='192.168.91.128', isHealth=true}], consul-8302: [ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=8302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=9302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=10302, address='192.168.91.128', isHealth=true}], thrift-rest-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8081], host='192.168.91.128', port=8081, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8082], host='192.168.91.128', port=8082, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8083], host='192.168.91.128', port=8083, address='192.168.91.128', isHealth=true}]]
2017-11-20 11:03:26.764  INFO 4052 --- [erListUpdater-0] t.c.l.ThriftConsulServerListLoadBalancer : Refreshed thrift serverList: [thrift-rpc-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25001], host='192.168.91.128', port=25001, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25002], host='192.168.91.128', port=25002, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25003], host='192.168.91.128', port=25003, address='192.168.91.128', isHealth=true}], consul-8301: [ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=8301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=9301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=10301, address='192.168.91.128', isHealth=true}], consul-8302: [ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=8302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=9302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=10302, address='192.168.91.128', isHealth=true}], thrift-rest-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8081], host='192.168.91.128', port=8081, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8082], host='192.168.91.128', port=8082, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8083], host='192.168.91.128', port=8083, address='192.168.91.128', isHealth=true}]]
2017-11-20 11:03:56.778  INFO 4052 --- [erListUpdater-0] t.c.l.ThriftConsulServerListLoadBalancer : Refreshed thrift serverList: [thrift-rpc-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25001], host='192.168.91.128', port=25001, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25002], host='192.168.91.128', port=25002, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rpc-calculator', tags=[thrift-rpc-calculator-25003], host='192.168.91.128', port=25003, address='192.168.91.128', isHealth=true}], consul-8301: [ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=8301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=9301, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8301', tags=[udp], host='192.168.91.128', port=10301, address='192.168.91.128', isHealth=true}], consul-8302: [ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=8302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=9302, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='consul-8302', tags=[udp], host='192.168.91.128', port=10302, address='192.168.91.128', isHealth=true}], thrift-rest-calculator: [ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8081], host='192.168.91.128', port=8081, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8082], host='192.168.91.128', port=8082, address='192.168.91.128', isHealth=true}, ThriftServerNode{node='node1', serviceId='thrift-rest-calculator', tags=[thrift-rest-calculator-8083], host='192.168.91.128', port=8083, address='192.168.91.128', isHealth=true}]]

```

### 本地测试：

访问本地`Thrift`客户端：

| 访问地址       | 参数arg1  | 参数arg2  | 页面输出结果  |
| :------------- |:-------------:|:-------------:|:-------------:|
| /rpc/add  | 200 | 100 | 300 | 
| /rpc/subtract  | 200 | 100 | 100 | 
| /rpc/multiply  | 200 | 100 | 20000 | 
| /rpc/division  | 200 | 100 | 2 | 

