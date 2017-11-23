package com.icekredit.rpc.thrift.examples.service.http.service;


import com.icekredit.rpc.thrift.examples.http.entities.Customer;

import java.util.List;

public interface CustomerService {

    Customer getCustomerById(String customerId);

    List<Customer> queryAllCustomers();

    void addNewCustomer(Customer customer);

    void modifyCustomerById(String customerId, Customer customer);

    Long getTotalDepositCards(String customerId);

}
