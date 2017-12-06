package com.icekredit.rpc.thrift.examples.thrift.controller;

import com.icekredit.rpc.thrift.client.annotation.ThriftReferer;
import com.icekredit.rpc.thrift.examples.http.entities.Customer;
import com.icekredit.rpc.thrift.examples.thrift.client.CustomerThriftClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rpc/customer")
public class CustomerRpcController {

    @ThriftReferer
    private CustomerThriftClient thriftClient;


    @GetMapping("/getCustomerById")
    public Customer getCustomerById(@RequestParam("customerId") String customerId) throws Exception {
        return Customer.fromThrift(thriftClient.client().getCustomerById(customerId));
    }

    @GetMapping("/queryAllCustomers")
    public List<Customer> queryAllCustomers() throws Exception {
        return thriftClient.client().queryAllCustomers()
                .stream().map(Customer::fromThrift)
                .collect(Collectors.toList());
    }

    @PostMapping("/addNewCustomer")
    public void addNewCustomer(Customer customer) throws Exception {
        thriftClient.client().addNewUser(customer.toThrift());
    }

    @GetMapping("/getTotalDepositCards")
    public Integer getTotalDepositCards(@RequestParam("customerId") String customerId) throws Exception {
        return thriftClient.client().getTotalDepositCard(customerId);
    }

}
