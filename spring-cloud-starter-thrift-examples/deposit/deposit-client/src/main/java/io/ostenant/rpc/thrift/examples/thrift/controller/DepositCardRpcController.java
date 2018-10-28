package io.ostenant.rpc.thrift.examples.thrift.controller;

import io.ostenant.rpc.thrift.client.annotation.ThriftRefer;
import io.ostenant.rpc.thrift.examples.http.entities.DepositCard;
import io.ostenant.rpc.thrift.examples.http.entities.DepositHistory;
import io.ostenant.rpc.thrift.examples.http.entities.WithdrawHistory;
import io.ostenant.rpc.thrift.examples.thrift.client.DepositCardThriftClient;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftDepositStatus;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftWithdrawStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rpc/deposit")
public class DepositCardRpcController {

    @ThriftRefer
    private DepositCardThriftClient thriftClient;

    @GetMapping("/queryAllDepositCards")
    public List<DepositCard> queryAllDepositCards(@RequestParam("customerId") String customerId)
            throws Exception {
        return thriftClient.client().queryAllDepositCards(customerId)
                .stream().map(DepositCard::fromThrift)
                .collect(Collectors.toList());
    }

    @PostMapping("/addNewDepositCard")
    public void addNewDepositCard(DepositCard depositCard) throws Exception {
        thriftClient.client().addNewDepositCard(depositCard.getCustomerId(), depositCard.toThrift());
    }

    @GetMapping("/depositMoney")
    public ThriftDepositStatus depositMoney(@RequestParam("depositCardId") String depositCardId,
                                            @RequestParam("money") Double money) throws Exception {
        return thriftClient.client().depositMoney(depositCardId, money);
    }

    @GetMapping("/withdrawMoney")
    public ThriftWithdrawStatus withdrawMoney(@RequestParam("depositCardId") String depositCardId,
                                              @RequestParam("money") Double money) throws Exception {
        return thriftClient.client().withdrawMoney(depositCardId, money);
    }

    @GetMapping("/queryDepositHistory")
    public List<DepositHistory> queryDepositHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return thriftClient.client().queryDepositHistorys(depositCardId)
                .stream().map(DepositHistory::fromThrift)
                .collect(Collectors.toList());
    }

    @GetMapping("/queryWithdrawHistory")
    public List<WithdrawHistory> queryWithdrawHistory(@RequestParam("depositCardId") String depositCardId)
            throws Exception {
        return thriftClient.client().queryWithdrawHistorys(depositCardId)
                .stream().map(WithdrawHistory::fromThrift)
                .collect(Collectors.toList());
    }
}
