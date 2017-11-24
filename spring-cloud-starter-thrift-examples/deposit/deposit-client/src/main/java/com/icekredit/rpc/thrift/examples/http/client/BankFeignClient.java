package com.icekredit.rpc.thrift.examples.http.client;

import com.icekredit.rpc.thrift.examples.http.service.IBankController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(serviceId = "deposit-http-service")
public interface BankFeignClient extends IBankController {
}
