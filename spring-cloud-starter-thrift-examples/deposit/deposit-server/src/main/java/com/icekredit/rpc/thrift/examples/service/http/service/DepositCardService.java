package com.icekredit.rpc.thrift.examples.service.http.service;


import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import com.icekredit.rpc.thrift.examples.http.entities.DepositHistory;
import com.icekredit.rpc.thrift.examples.http.entities.WithdrawHistory;
import com.icekredit.rpc.thrift.examples.http.enums.DepositStatus;
import com.icekredit.rpc.thrift.examples.http.enums.WithdrawStatus;

import java.util.List;

public interface DepositCardService {

    List<DepositCard> queryAllDepositCards(String customerId);

    void addNewDepositCard(String customerId, DepositCard depositCard);

    DepositStatus depositMoney(String depositCardId, double money);

    WithdrawStatus withdrawMoney(String depositCardId, double money);

    List<DepositHistory> queryDepositHistorys(String depositCardId);

    List<WithdrawHistory> queryWithdrawHistorys(String depositCardId);

}
