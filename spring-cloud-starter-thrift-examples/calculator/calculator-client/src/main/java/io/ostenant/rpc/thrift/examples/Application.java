package io.ostenant.rpc.thrift.examples;

import io.ostenant.rpc.thrift.client.annotation.EnableThriftClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableThriftClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
// java -jar target/calculator-server-1.0.0.jar --spring.profiles.active=25000
// java -jar target/calculator-server-1.0.0.jar --spring.profiles.active=25001
// java -jar target/calculator-server-1.0.0.jar --spring.profiles.active=25002
// java -jar target/calculator-client-1.0.0.jar --spring.profiles.active=8080