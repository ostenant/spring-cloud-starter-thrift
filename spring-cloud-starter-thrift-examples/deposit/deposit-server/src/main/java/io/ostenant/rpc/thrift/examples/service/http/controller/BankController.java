package io.ostenant.rpc.thrift.examples.service.http.controller;

import io.ostenant.rpc.thrift.examples.http.entities.Bank;
import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import io.ostenant.rpc.thrift.examples.http.enums.Region;
import io.ostenant.rpc.thrift.examples.http.service.IBankController;
import io.ostenant.rpc.thrift.examples.service.http.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/http/bank")
public class BankController implements IBankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/addNewBank")
    public void addNewBank(Bank bank) throws Exception {
        bankService.registerNewBank(bank);
    }

    @GetMapping("/getBankById")
    public Bank getBankById(@RequestParam("bankId") Long bankId) throws Exception {
        return bankService.getBankById(bankId);
    }

    @GetMapping("/queryAllBranchesByRegion")
    public Map<Region, List<Branch>> queryAllBranchesByRegion(
            @RequestParam("bankId") Long bankId) throws Exception {
        return bankService.queryAllBranchesByRegion(bankId);
    }

}
