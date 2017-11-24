package com.icekredit.rpc.thrift.examples.http.service;

import com.icekredit.rpc.thrift.examples.http.entities.Bank;
import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import com.icekredit.rpc.thrift.examples.http.enums.Region;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping("/http/bank")
public interface IBankController {

    @PostMapping("/addNewBank")
    void addNewBank(Bank bank) throws Exception;

    @GetMapping("/getBankById")
    Bank getBankById(@RequestParam("bankId") Long bankId) throws Exception;

    @GetMapping("/queryAllBranchesByRegion")
    Map<Region, List<Branch>> queryAllBranchesByRegion(
            @RequestParam("bankId") Long bankId) throws Exception;

}
