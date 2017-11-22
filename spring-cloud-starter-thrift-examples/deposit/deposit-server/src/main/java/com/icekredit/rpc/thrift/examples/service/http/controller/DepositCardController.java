package com.icekredit.rpc.thrift.examples.service.http.controller;

import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import com.icekredit.rpc.thrift.examples.service.http.service.DepositCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/http/depositCard")
public class DepositCardController {

    private final DepositCardService depositCardService;

    @Autowired
    public DepositCardController(DepositCardService depositCardService) {
        this.depositCardService = depositCardService;
    }

    @GetMapping("/queryAllDepositCards")
    public List<DepositCard> queryAllDepositCards(@RequestParam("customerId") String customerId) {
        return depositCardService.queryAllDepositCards(customerId);
    }

    @PostMapping("/addNewDepositCard")
    public void addNewDepositCard(DepositCard depositCard) {
        depositCardService.addNewDepositCard(depositCard.getCustomerId(), depositCard);
    }

    @GetMapping("/depositMoney")
    public Integer depositMoney(@RequestParam("depositCardId") String depositCardId,
                                @RequestParam("money") Double money) {
        depositCardService.depositMoney(depositCardId, money);
        return null;
    }

    @GetMapping("/withdrawMoney")
    public Integer withdrawMoney(@RequestParam("depositCardId") String depositCardId,
                                 @RequestParam("money") Double money) {
        depositCardService.withdrawMoney(depositCardId, money);
        return null;
    }

}
