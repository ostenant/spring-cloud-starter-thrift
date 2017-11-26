package com.icekredit.rpc.thrift.examples.thrift.client;

import com.icekredit.rpc.thrift.client.annotation.ThriftClient;
import com.icekredit.rpc.thrift.client.common.ThriftClientAware;
import com.icekredit.rpc.thrift.examples.thrift.service.ThriftCustomerService;

@ThriftClient(serviceId = "deposit-server-rpc", refer = ThriftCustomerService.class)
public interface CustomerThriftClient extends ThriftClientAware<ThriftCustomerService.Client> {

}
