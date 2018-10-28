package io.ostenant.rpc.thrift.examples.thrift.client;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.examples.thrift.service.ThriftCustomerService;

@ThriftClient(serviceId = "deposit-server-rpc", refer = ThriftCustomerService.class)
public interface CustomerThriftClient extends ThriftClientAware<ThriftCustomerService.Client> {

}
