package com.icekredit.rpc.thrift.examples.http.client;

import com.icekredit.rpc.thrift.examples.http.service.ICustomerController;
import org.springframework.cloud.netflix.feign.FeignClient;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@FeignClient(serviceId = "deposit-server-rest")
public interface CustomerFeignClient extends ICustomerController {
}
