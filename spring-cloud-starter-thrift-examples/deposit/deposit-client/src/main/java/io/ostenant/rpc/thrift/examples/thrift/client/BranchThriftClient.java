package io.ostenant.rpc.thrift.examples.thrift.client;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.examples.thrift.service.ThriftBranchService;

@ThriftClient(serviceId = "deposit-server-rpc", refer = ThriftBranchService.class)
public interface BranchThriftClient extends ThriftClientAware<ThriftBranchService.Client> {

}
