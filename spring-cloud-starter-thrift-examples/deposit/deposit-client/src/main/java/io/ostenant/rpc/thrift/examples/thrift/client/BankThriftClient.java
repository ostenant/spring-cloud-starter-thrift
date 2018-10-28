package io.ostenant.rpc.thrift.examples.thrift.client;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.examples.thrift.service.ThriftBankService;

@ThriftClient(serviceId = "deposit-server-rpc", refer = ThriftBankService.class)
public interface BankThriftClient extends ThriftClientAware<ThriftBankService.Client> {

}
