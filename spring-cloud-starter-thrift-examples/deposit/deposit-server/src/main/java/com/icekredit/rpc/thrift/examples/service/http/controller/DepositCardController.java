package com.icekredit.rpc.thrift.examples.service.http.controller;

import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import com.icekredit.rpc.thrift.examples.http.entities.DepositHistory;
import com.icekredit.rpc.thrift.examples.http.entities.WithdrawHistory;
import com.icekredit.rpc.thrift.examples.http.enums.DepositStatus;
import com.icekredit.rpc.thrift.examples.http.enums.WithdrawStatus;
import com.icekredit.rpc.thrift.examples.http.service.IDepositCardController;
import com.icekredit.rpc.thrift.examples.service.http.service.DepositCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/http/depositCard")
public class DepositCardController implements IDepositCardController {

    private final DepositCardService depositCardService;

    @Autowired
    public DepositCardController(DepositCardService depositCardService) {
        this.depositCardService = depositCardService;
    }

    @GetMapping("/queryAllDepositCards")
    public List<DepositCard> queryAllDepositCards(@RequestParam("customerId") String customerId) throws Exception {
        return depositCardService.queryAllDepositCards(customerId);
    }

    @PostMapping("/addNewDepositCard")
    public void addNewDepositCard(DepositCard depositCard) throws Exception {
        depositCardService.addNewDepositCard(depositCard.getCustomerId(), depositCard);
    }

    @GetMapping("/depositMoney")
    public DepositStatus depositMoney(@RequestParam("depositCardId") String depositCardId,
                                      @RequestParam("money") Double money) throws Exception {
        return depositCardService.depositMoney(depositCardId, money);
    }

    @GetMapping("/withdrawMoney")
    public WithdrawStatus withdrawMoney(@RequestParam("depositCardId") String depositCardId,
                                        @RequestParam("money") Double money) throws Exception {
        return depositCardService.withdrawMoney(depositCardId, money);
    }

    @GetMapping("/queryDepositHistory")
    public List<DepositHistory> queryDepositHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return depositCardService.queryDepositHistorys(depositCardId);
    }

    @GetMapping("/queryWithdrawHistory")
    public List<WithdrawHistory> queryWithdrawHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return depositCardService.queryWithdrawHistorys(depositCardId);
    }


}
