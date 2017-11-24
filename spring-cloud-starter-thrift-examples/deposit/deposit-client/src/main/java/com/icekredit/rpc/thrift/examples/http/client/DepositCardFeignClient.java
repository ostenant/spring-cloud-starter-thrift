package com.icekredit.rpc.thrift.examples.http.client;

import com.icekredit.rpc.thrift.examples.http.service.IDepositCardController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(serviceId = "deposit-http-service")
public interface DepositCardFeignClient extends IDepositCardController {
}
