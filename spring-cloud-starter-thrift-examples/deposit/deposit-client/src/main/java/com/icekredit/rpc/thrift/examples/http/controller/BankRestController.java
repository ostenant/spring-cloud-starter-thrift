package com.icekredit.rpc.thrift.examples.http.controller;

import com.icekredit.rpc.thrift.examples.http.client.BankFeignClient;
import com.icekredit.rpc.thrift.examples.http.entities.Bank;
import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import com.icekredit.rpc.thrift.examples.http.enums.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/bank")
public class BankRestController {

    private final BankFeignClient feignClient;

    @Autowired
    public BankRestController(BankFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @PostMapping("/addNewBank")
    public void addNewBank(Bank bank) throws Exception {
        feignClient.addNewBank(bank);
    }

    @GetMapping("/getBankById")
    public Bank getBankById(@RequestParam("bankId") Long bankId) throws Exception {
        return feignClient.getBankById(bankId);
    }

    @GetMapping("/queryAllBranchesByRegion")
    public Map<Region, List<Branch>> queryAllBranchesByRegion(
            @RequestParam("bankId") Long bankId) throws Exception {
        return feignClient.queryAllBranchesByRegion(bankId);
    }

}
