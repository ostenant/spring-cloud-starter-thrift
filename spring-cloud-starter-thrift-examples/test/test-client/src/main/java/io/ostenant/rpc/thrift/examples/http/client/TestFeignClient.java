package io.ostenant.rpc.thrift.examples.http.client;

import io.ostenant.rpc.thrift.examples.http.ITestController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(serviceId = "test-server-rest")
public interface TestFeignClient extends ITestController {

}
