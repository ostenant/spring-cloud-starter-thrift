package io.ostenant.rpc.thrift.examples.http.client;

import io.ostenant.rpc.thrift.examples.http.service.IBranchController;
import org.springframework.cloud.netflix.feign.FeignClient;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@FeignClient(serviceId = "deposit-server-rest")
public interface BranchFeignClient extends IBranchController {
}
