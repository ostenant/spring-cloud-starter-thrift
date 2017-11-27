package com.icekredit.rpc.thrift.examples.thrift.client;

import com.icekredit.rpc.thrift.client.annotation.ThriftClient;
import com.icekredit.rpc.thrift.client.common.ThriftClientAware;
import com.icekredit.rpc.thrift.examples.thrift.TestService;

@ThriftClient(serviceId = "test-server-rpc", refer = TestService.class)
public interface TestThriftClient extends ThriftClientAware<TestService.Client> {
}
