package com.icekredit.rpc.thrift.examples.service.http.service.impl;

import com.icekredit.rpc.thrift.examples.http.entities.Customer;
import com.icekredit.rpc.thrift.examples.mapper.CustomerMapper;
import com.icekredit.rpc.thrift.examples.mapper.DepositCardMapper;
import com.icekredit.rpc.thrift.examples.service.http.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final DepositCardMapper depositCardMapper;

    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper, DepositCardMapper depositCardMapper) {
        this.customerMapper = customerMapper;
        this.depositCardMapper = depositCardMapper;
    }

    @Override
    public Customer getCustomerById(String customerId) {
        return customerMapper.findById(customerId);
    }

    @Override
    public List<Customer> queryAllCustomers() {
        return customerMapper.findAll();
    }

    @Override
    @Transactional
    public void addNewCustomer(Customer customer) {
        customerMapper.save(customer);
    }

    @Override
    @Transactional
    public void modifyCustomerById(String customerId, Customer customer) {
        customer.setId(customerId);
        customerMapper.update(customer);
    }

    @Override
    public Long getTotalDepositCards(String customerId) {
        return depositCardMapper.countRowsByCustomerId(customerId);
    }

}
