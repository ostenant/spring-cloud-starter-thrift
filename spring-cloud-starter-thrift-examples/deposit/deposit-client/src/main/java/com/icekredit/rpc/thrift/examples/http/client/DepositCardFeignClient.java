package com.icekredit.rpc.thrift.examples.http.client;

import com.icekredit.rpc.thrift.examples.http.service.IDepositCardController;
import org.springframework.cloud.netflix.feign.FeignClient;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@FeignClient(serviceId = "deposit-server-rest")
public interface DepositCardFeignClient extends IDepositCardController {
}
