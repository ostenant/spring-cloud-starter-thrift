package io.ostenant.rpc.thrift.examples.rpc;

import io.ostenant.rpc.thrift.client.annotation.ThriftClient;
import io.ostenant.rpc.thrift.client.common.ThriftClientAware;
import io.ostenant.rpc.thrift.examples.thrift.CalculatorService;

@ThriftClient(serviceId = "thrift-calculator-rpc-server", refer = CalculatorService.class, version = 2.0)
public interface CalculatorThriftClient extends ThriftClientAware<CalculatorService.Client> {
}
