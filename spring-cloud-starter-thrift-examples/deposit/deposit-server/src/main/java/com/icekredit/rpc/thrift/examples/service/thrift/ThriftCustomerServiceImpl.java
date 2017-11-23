package com.icekredit.rpc.thrift.examples.service.thrift;

import com.icekredit.rpc.thrift.examples.http.entities.Customer;
import com.icekredit.rpc.thrift.examples.mapper.CustomerMapper;
import com.icekredit.rpc.thrift.examples.mapper.DepositCardMapper;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftCustomer;
import com.icekredit.rpc.thrift.examples.thrift.service.ThriftCustomerService;
import com.icekredit.rpc.thrift.server.annotation.ThriftService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ThriftService(name = "thriftCustomerService")
public class ThriftCustomerServiceImpl implements ThriftCustomerService.Iface {

    private final CustomerMapper customerMapper;
    private final DepositCardMapper depositCardMapper;

    @Autowired
    public ThriftCustomerServiceImpl(CustomerMapper customerMapper, DepositCardMapper depositCardMapper) {
        this.customerMapper = customerMapper;
        this.depositCardMapper = depositCardMapper;
    }

    @Override
    public ThriftCustomer getCustomerById(String customerId) throws TException {
        Customer customer = customerMapper.findById(customerId);
        return customer.toThrift();
    }

    @Override
    public List<ThriftCustomer> queryAllCustomers() throws TException {
        List<Customer> customerList = customerMapper.findAll();
        return customerList.stream().map(Customer::toThrift).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void addNewUser(ThriftCustomer customer) throws TException {
        Customer newCustomer = Customer.fromThrift(customer);
        customerMapper.save(newCustomer);
    }

    @Override
    @Transactional
    public void modifyUserById(String customerId, ThriftCustomer customer) throws TException {
        Customer modifiedCustomer = Customer.fromThrift(customer);
        modifiedCustomer.setId(customerId);
        customerMapper.update(modifiedCustomer);
    }

    @Override
    public int getTotalDepositCard(String customerId) throws TException {
        return depositCardMapper.countRowsByCustomerId(customerId).intValue();
    }

}
