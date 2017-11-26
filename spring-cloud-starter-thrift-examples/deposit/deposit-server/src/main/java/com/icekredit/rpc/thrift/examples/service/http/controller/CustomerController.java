package com.icekredit.rpc.thrift.examples.service.http.controller;

import com.icekredit.rpc.thrift.examples.http.entities.Customer;
import com.icekredit.rpc.thrift.examples.http.service.ICustomerController;
import com.icekredit.rpc.thrift.examples.service.http.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/http/customer")
public class CustomerController implements ICustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomerById")
    public Customer getCustomerById(@RequestParam("customerId") String customerId) {
        return customerService.getCustomerById(customerId);
    }

    @GetMapping("/queryAllCustomers")
    public List<Customer> queryAllCustomers() {
        return customerService.queryAllCustomers();
    }

    @PostMapping("/addNewCustomer")
    public void addNewCustomer(Customer customer) {
        customerService.addNewCustomer(customer);
    }

    @GetMapping("/getTotalDepositCards")
    public Long getTotalDepositCards(@RequestParam("customerId") String customerId) {
        return customerService.getTotalDepositCards(customerId);
    }

}
