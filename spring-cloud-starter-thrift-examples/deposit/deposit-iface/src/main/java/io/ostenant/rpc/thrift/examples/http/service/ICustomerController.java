package io.ostenant.rpc.thrift.examples.http.service;


import io.ostenant.rpc.thrift.examples.http.entities.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/http/customer")
public interface ICustomerController {

    @GetMapping("/getCustomerById")
    Customer getCustomerById(@RequestParam("customerId") String customerId);

    @GetMapping("/queryAllCustomers")
    List<Customer> queryAllCustomers();

    @PostMapping("/addNewCustomer")
    void addNewCustomer(Customer customer);

    @GetMapping("/getTotalDepositCards")
    Long getTotalDepositCards(@RequestParam("customerId") String customerId);

}
