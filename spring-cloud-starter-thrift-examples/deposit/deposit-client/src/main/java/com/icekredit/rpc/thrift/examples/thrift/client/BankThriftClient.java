package com.icekredit.rpc.thrift.examples.thrift.client;

import com.icekredit.rpc.thrift.client.annotation.ThriftClient;
import com.icekredit.rpc.thrift.client.common.ThriftClientAware;
import com.icekredit.rpc.thrift.examples.thrift.service.ThriftBankService;

@ThriftClient(serviceId = "deposit-thrift-service", refer = ThriftBankService.class)
public interface BankThriftClient extends ThriftClientAware<ThriftBankService.Client> {

}
