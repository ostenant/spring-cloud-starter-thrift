package io.ostenant.rpc.thrift.examples.thrift.client;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.examples.thrift.TestService;

@ThriftClient(serviceId = "test-server-rpc", refer = TestService.class)
public interface TestThriftClient extends ThriftClientAware<TestService.Client> {
}
