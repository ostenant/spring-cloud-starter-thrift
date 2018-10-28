package io.ostenant.rpc.thrift.examples.thrift.client;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.examples.thrift.service.ThriftDepositCardService;

@ThriftClient(serviceId = "deposit-server-rpc", refer = ThriftDepositCardService.class)
public interface DepositCardThriftClient extends ThriftClientAware<ThriftDepositCardService.Client> {

}
