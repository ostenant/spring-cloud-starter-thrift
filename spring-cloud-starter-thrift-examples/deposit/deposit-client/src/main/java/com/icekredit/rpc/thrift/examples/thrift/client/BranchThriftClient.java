package com.icekredit.rpc.thrift.examples.thrift.client;

import com.icekredit.rpc.thrift.client.annotation.ThriftClient;
import com.icekredit.rpc.thrift.client.common.ThriftClientAware;
import com.icekredit.rpc.thrift.examples.thrift.service.ThriftBranchService;

@ThriftClient(serviceId = "deposit-server-rpc", refer = ThriftBranchService.class)
public interface BranchThriftClient extends ThriftClientAware<ThriftBranchService.Client> {

}
