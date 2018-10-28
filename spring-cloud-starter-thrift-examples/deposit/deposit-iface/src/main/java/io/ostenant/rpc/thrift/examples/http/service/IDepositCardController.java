package io.ostenant.rpc.thrift.examples.http.service;


import io.ostenant.rpc.thrift.examples.http.entities.DepositCard;
import io.ostenant.rpc.thrift.examples.http.entities.DepositHistory;
import io.ostenant.rpc.thrift.examples.http.entities.WithdrawHistory;
import io.ostenant.rpc.thrift.examples.http.enums.DepositStatus;
import io.ostenant.rpc.thrift.examples.http.enums.WithdrawStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/http/depositCard")
public interface IDepositCardController {

    @GetMapping("/queryAllDepositCards")
    List<DepositCard> queryAllDepositCards(@RequestParam("customerId") String customerId) throws Exception;

    @PostMapping("/addNewDepositCard")
    void addNewDepositCard(DepositCard depositCard) throws Exception;

    @GetMapping("/depositMoney")
    DepositStatus depositMoney(@RequestParam("depositCardId") String depositCardId,
                               @RequestParam("money") Double money) throws Exception;

    @GetMapping("/withdrawMoney")
    WithdrawStatus withdrawMoney(@RequestParam("depositCardId") String depositCardId,
                                 @RequestParam("money") Double money) throws Exception;

    @GetMapping("/queryDepositHistory")
    List<DepositHistory> queryDepositHistory(@RequestParam("depositCardId") String depositCardId) throws Exception;

    @GetMapping("/queryWithdrawHistory")
    List<WithdrawHistory> queryWithdrawHistory(@RequestParam("depositCardId") String depositCardId) throws Exception;

}
