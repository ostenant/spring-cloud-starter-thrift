package io.ostenant.rpc.thrift.examples.thrift.controller;

import io.ostenant.rpc.thrift.client.annotation.ThriftRefer;
import io.ostenant.rpc.thrift.examples.http.entities.Customer;
import io.ostenant.rpc.thrift.examples.thrift.client.CustomerThriftClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rpc/customer")
public class CustomerRpcController {

    @ThriftRefer
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
