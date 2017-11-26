package com.icekredit.rpc.thrift.examples.service.http.service.impl;

import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import com.icekredit.rpc.thrift.examples.http.entities.DepositHistory;
import com.icekredit.rpc.thrift.examples.http.entities.WithdrawHistory;
import com.icekredit.rpc.thrift.examples.http.enums.DepositStatus;
import com.icekredit.rpc.thrift.examples.http.enums.WithdrawStatus;
import com.icekredit.rpc.thrift.examples.mapper.DepositCardMapper;
import com.icekredit.rpc.thrift.examples.mapper.DepositHistoryMapper;
import com.icekredit.rpc.thrift.examples.mapper.WithdrawHistoryMapper;
import com.icekredit.rpc.thrift.examples.service.http.service.DepositCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DepositCardServiceImpl implements DepositCardService {

    private final DepositCardMapper depositCardMapper;
    private final DepositHistoryMapper depositHistoryMapper;
    private final WithdrawHistoryMapper withdrawHistoryMapper;

    @Autowired
    public DepositCardServiceImpl(DepositCardMapper depositCardMapper, DepositHistoryMapper depositHistoryMapper, WithdrawHistoryMapper withdrawHistoryMapper) {
        this.depositCardMapper = depositCardMapper;
        this.depositHistoryMapper = depositHistoryMapper;
        this.withdrawHistoryMapper = withdrawHistoryMapper;
    }

    @Override
    public List<DepositCard> queryAllDepositCards(String customerId) {
        return depositCardMapper.queryAllDepositCards(customerId);
    }

    @Override
    @Transactional
    public void addNewDepositCard(String customerId, DepositCard depositCard) {
        depositCard.setCustomerId(customerId);
        depositCardMapper.save(depositCard);
    }

    @Override
    @Transactional
    public DepositStatus depositMoney(String depositCardId, double money) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            DepositHistory depositHistory = new DepositHistory();
            depositHistory.setSerialNumber(UUID.randomUUID().toString().replace("-", ""));
            depositHistory.setSubmittedTime(sf.format(new Date()));

            depositCardMapper.decrementMoney(depositCardId, money);
            depositHistory.setFinishedTime(sf.format(new Date()));

            depositHistory.setTransactionAmount(money);
            depositHistory.setDepositCardId(depositCardId);
            depositHistory.setStatus(1);

            depositHistoryMapper.save(depositHistory);

            return DepositStatus.FINISHED;
        } catch (Exception e) {
            e.printStackTrace();
            return DepositStatus.FAILED;
        }
    }

    @Override
    @Transactional
    public WithdrawStatus withdrawMoney(String depositCardId, double money) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            WithdrawHistory withdrawHistory = new WithdrawHistory();
            withdrawHistory.setSerialNumber(UUID.randomUUID().toString().replace("-", ""));
            withdrawHistory.setSubmittedTime(sf.format(new Date()));

            depositCardMapper.decrementMoney(depositCardId, money);
            withdrawHistory.setFinishedTime(sf.format(new Date()));

            withdrawHistory.setTransactionAmount(money);
            withdrawHistory.setDepositCardId(depositCardId);
            withdrawHistory.setStatus(1);

            withdrawHistoryMapper.save(withdrawHistory);

            return WithdrawStatus.FINISHED;
        } catch (Exception e) {
            e.printStackTrace();
            return WithdrawStatus.FAILED;
        }
    }

    @Override
    public List<DepositHistory> queryDepositHistorys(String depositCardId) {
        return depositHistoryMapper.queryDepositHistoryList(depositCardId);
    }

    @Override
    public List<WithdrawHistory> queryWithdrawHistorys(String depositCardId) {
        return withdrawHistoryMapper.queryWithdrawHistoryList(depositCardId);
    }

}
