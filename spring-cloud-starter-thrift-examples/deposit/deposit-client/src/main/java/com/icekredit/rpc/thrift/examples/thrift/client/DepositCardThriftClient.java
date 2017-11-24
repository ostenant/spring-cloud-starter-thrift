package com.icekredit.rpc.thrift.examples.thrift.client;

import com.icekredit.rpc.thrift.client.annotation.ThriftClient;
import com.icekredit.rpc.thrift.client.common.ThriftClientAware;
import com.icekredit.rpc.thrift.examples.thrift.service.ThriftDepositCardService;

@ThriftClient(serviceId = "deposit-thrift-service", refer = ThriftDepositCardService.class)
public interface DepositCardThriftClient extends ThriftClientAware<ThriftDepositCardService.Client> {

}
