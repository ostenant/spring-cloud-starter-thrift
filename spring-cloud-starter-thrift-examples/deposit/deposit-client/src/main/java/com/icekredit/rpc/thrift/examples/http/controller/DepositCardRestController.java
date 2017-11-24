package com.icekredit.rpc.thrift.examples.http.controller;

import com.icekredit.rpc.thrift.examples.http.client.DepositCardFeignClient;
import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import com.icekredit.rpc.thrift.examples.http.entities.DepositHistory;
import com.icekredit.rpc.thrift.examples.http.entities.WithdrawHistory;
import com.icekredit.rpc.thrift.examples.http.enums.DepositStatus;
import com.icekredit.rpc.thrift.examples.http.enums.WithdrawStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/deposit")
public class DepositCardRestController {

    private final DepositCardFeignClient feignClient;

    @Autowired
    public DepositCardRestController(DepositCardFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @GetMapping("/queryAllDepositCards")
    public List<DepositCard> queryAllDepositCards(@RequestParam("customerId") String customerId)
            throws Exception {
        return feignClient.queryAllDepositCards(customerId);
    }

    @PostMapping("/addNewDepositCard")
    public void addNewDepositCard(DepositCard depositCard) throws Exception {
        feignClient.addNewDepositCard(depositCard);
    }

    @GetMapping("/depositMoney")
    public DepositStatus depositMoney(@RequestParam("depositCardId") String depositCardId,
                                      @RequestParam("money") Double money) throws Exception {
        return feignClient.depositMoney(depositCardId, money);
    }

    @GetMapping("/withdrawMoney")
    public WithdrawStatus withdrawMoney(@RequestParam("depositCardId") String depositCardId,
                                        @RequestParam("money") Double money) throws Exception {
        return feignClient.withdrawMoney(depositCardId, money);
    }

    @GetMapping("/queryDepositHistory")
    public List<DepositHistory> queryDepositHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return feignClient.queryDepositHistory(depositCardId);
    }

    @GetMapping("/queryWithdrawHistory")
    public List<WithdrawHistory> queryWithdrawHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return feignClient.queryWithdrawHistory(depositCardId);
    }
}
