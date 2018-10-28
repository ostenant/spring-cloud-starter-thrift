package io.ostenant.rpc.thrift.examples.service.http.service;


import io.ostenant.rpc.thrift.examples.http.entities.DepositCard;
import io.ostenant.rpc.thrift.examples.http.entities.DepositHistory;
import io.ostenant.rpc.thrift.examples.http.entities.WithdrawHistory;
import io.ostenant.rpc.thrift.examples.http.enums.DepositStatus;
import io.ostenant.rpc.thrift.examples.http.enums.WithdrawStatus;

import java.util.List;

public interface DepositCardService {

    List<DepositCard> queryAllDepositCards(String customerId);

    void addNewDepositCard(String customerId, DepositCard depositCard);

    DepositStatus depositMoney(String depositCardId, double money);

    WithdrawStatus withdrawMoney(String depositCardId, double money);

    List<DepositHistory> queryDepositHistorys(String depositCardId);

    List<WithdrawHistory> queryWithdrawHistorys(String depositCardId);

}
