package io.ostenant.rpc.thrift.examples.http.controller;

import io.ostenant.rpc.thrift.examples.http.client.CustomerFeignClient;
import io.ostenant.rpc.thrift.examples.http.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/customer")
public class CustomerRestController {

    private final CustomerFeignClient feignClient;

    @Autowired
    public CustomerRestController(CustomerFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @GetMapping("/getCustomerById")
    public Customer getCustomerById(@RequestParam("customerId") String customerId) {
        return feignClient.getCustomerById(customerId);
    }

    @GetMapping("/queryAllCustomers")
    public List<Customer> queryAllCustomers() {
        return feignClient.queryAllCustomers();
    }

    @PostMapping("/addNewCustomer")
    public void addNewCustomer(Customer customer) {
        feignClient.addNewCustomer(customer);
    }

    @GetMapping("/getTotalDepositCards")
    public Long getTotalDepositCards(@RequestParam("customerId") String customerId) {
        return feignClient.getTotalDepositCards(customerId);
    }

}
