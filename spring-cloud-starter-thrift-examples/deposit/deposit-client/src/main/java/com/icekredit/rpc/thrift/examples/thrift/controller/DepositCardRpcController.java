package com.icekredit.rpc.thrift.examples.thrift.controller;

import com.icekredit.rpc.thrift.client.annotation.ThriftReferer;
import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import com.icekredit.rpc.thrift.examples.http.entities.DepositHistory;
import com.icekredit.rpc.thrift.examples.http.entities.WithdrawHistory;
import com.icekredit.rpc.thrift.examples.thrift.client.DepositCardThriftClient;
import com.icekredit.rpc.thrift.examples.thrift.enums.ThriftDepositStatus;
import com.icekredit.rpc.thrift.examples.thrift.enums.ThriftWithdrawStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rpc/deposit")
public class DepositCardRpcController {

    @ThriftReferer
    DepositCardThriftClient thriftClient;

    @GetMapping("/queryAllDepositCards")
    public List<DepositCard> queryAllDepositCards(@RequestParam("customerId") String customerId)
            throws Exception {
        return thriftClient.thriftClient().queryAllDepositCards(customerId)
                .stream().map(DepositCard::fromThrift)
                .collect(Collectors.toList());
    }

    @PostMapping("/addNewDepositCard")
    public void addNewDepositCard(DepositCard depositCard) throws Exception {
        thriftClient.thriftClient().addNewDepositCard(depositCard.getCustomerId(), depositCard.toThrift());
    }

    @GetMapping("/depositMoney")
    public ThriftDepositStatus depositMoney(@RequestParam("depositCardId") String depositCardId,
                                            @RequestParam("money") Double money) throws Exception {
        return thriftClient.thriftClient().depositMoney(depositCardId, money);
    }

    @GetMapping("/withdrawMoney")
    public ThriftWithdrawStatus withdrawMoney(@RequestParam("depositCardId") String depositCardId,
                                              @RequestParam("money") Double money) throws Exception {
        return thriftClient.thriftClient().withdrawMoney(depositCardId, money);
    }

    @GetMapping("/queryDepositHistory")
    public List<DepositHistory> queryDepositHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return thriftClient.thriftClient().queryDepositHistorys(depositCardId)
                .stream().map(DepositHistory::fromThrift)
                .collect(Collectors.toList());
    }

    @GetMapping("/queryWithdrawHistory")
    public List<WithdrawHistory> queryWithdrawHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return thriftClient.thriftClient().queryWithdrawHistorys(depositCardId)
                .stream().map(WithdrawHistory::fromThrift)
                .collect(Collectors.toList());
    }
}
