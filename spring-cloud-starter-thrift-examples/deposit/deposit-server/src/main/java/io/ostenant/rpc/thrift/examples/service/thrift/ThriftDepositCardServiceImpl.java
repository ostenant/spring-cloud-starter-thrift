package io.ostenant.rpc.thrift.examples.service.thrift;

import io.ostenant.rpc.thrift.examples.http.entities.*;
import io.ostenant.rpc.thrift.examples.mapper.*;
import io.ostenant.rpc.thrift.examples.thrift.entities.*;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftDepositStatus;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftWithdrawStatus;
import io.ostenant.rpc.thrift.examples.thrift.service.ThriftDepositCardService;
import io.ostenant.rpc.thrift.server.annotation.ThriftService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@ThriftService(name = "thriftDepositCardService")
public class ThriftDepositCardServiceImpl implements ThriftDepositCardService.Iface {

    private final BranchMapper branchMapper;
    private final DepositCardMapper depositCardMapper;
    private final CustomerMapper customerMapper;
    private final DepositHistoryMapper depositHistoryMapper;
    private final WithdrawHistoryMapper withdrawHistoryMapper;

    @Autowired
    public ThriftDepositCardServiceImpl(BranchMapper branchMapper, DepositCardMapper depositCardMapper, CustomerMapper customerMapper, DepositHistoryMapper depositHistoryMapper, WithdrawHistoryMapper withdrawHistoryMapper) {
        this.branchMapper = branchMapper;
        this.depositCardMapper = depositCardMapper;
        this.customerMapper = customerMapper;
        this.depositHistoryMapper = depositHistoryMapper;
        this.withdrawHistoryMapper = withdrawHistoryMapper;
    }

    @Override
    public Set<ThriftDepositCard> queryAllDepositCards(String customerId) throws TException {
        List<DepositCard> depositCardList = depositCardMapper.queryAllDepositCards(customerId);
        return depositCardList.stream().map(depositCard -> {
            ThriftDepositCard thriftDepositCard = depositCard.toThrift();

            Long branchId = depositCard.getBranchId();
            if (Objects.nonNull(branchId) && branchId > 0L) {
                Branch branch = branchMapper.findById(branchId);
                ThriftBranch thriftBranch = branch.toThrift();

                ThriftBank thriftBank = new ThriftBank();
                thriftBank.setId(branch.getBankId());
                thriftBranch.setBank(thriftBank);

                thriftDepositCard.setBranch(thriftBranch);
            }

            Customer customer = customerMapper.findById(customerId);
            ThriftCustomer thriftCustomer = customer.toThrift();
            thriftDepositCard.setCustomer(thriftCustomer);

            return thriftDepositCard;
        }).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void addNewDepositCard(String customerId, ThriftDepositCard depositCard) throws TException {
        DepositCard newDepositCard = DepositCard.fromThrift(depositCard);
        depositCardMapper.save(newDepositCard);
    }

    @Override
    @Transactional
    public ThriftDepositStatus depositMoney(String depositCardId, double money) throws TException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            DepositHistory depositHistory = new DepositHistory();
            depositHistory.setSubmittedTime(sf.format(new Date()));

            depositCardMapper.incrementMoney(depositCardId, money);
            depositHistory.setFinishedTime(sf.format(new Date()));

            depositHistory.setSerialNumber(UUID.randomUUID().toString().replace("-", ""));
            depositHistory.setTransactionAmount(money);
            depositHistory.setDepositCardId(depositCardId);
            depositHistory.setStatus(1);

            depositHistoryMapper.save(depositHistory);
            return ThriftDepositStatus.FINISHED;
        } catch (Exception e) {
            e.printStackTrace();
            return ThriftDepositStatus.FAILED;
        }
    }

    @Override
    @Transactional
    public ThriftWithdrawStatus withdrawMoney(String depositCardId, double money) throws TException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            WithdrawHistory withdrawHistory = new WithdrawHistory();
            withdrawHistory.setSubmittedTime(sf.format(new Date()));

            depositCardMapper.decrementMoney(depositCardId, money);
            withdrawHistory.setFinishedTime(sf.format(new Date()));

            withdrawHistory.setSerialNumber(UUID.randomUUID().toString().replace("-", ""));
            withdrawHistory.setTransactionAmount(money);
            withdrawHistory.setDepositCardId(depositCardId);
            withdrawHistory.setStatus(1);

            withdrawHistoryMapper.save(withdrawHistory);
            return ThriftWithdrawStatus.FINISHED;
        } catch (Exception e) {
            e.printStackTrace();
            return ThriftWithdrawStatus.FAILED;
        }
    }

    @Override
    public List<ThriftDeposit> queryDepositHistorys(String depositCardId) throws TException {
        List<DepositHistory> depositHistory = depositHistoryMapper.queryDepositHistoryList(depositCardId);
        return depositHistory.stream().map(DepositHistory::toThrift).collect(Collectors.toList());
    }

    @Override
    public List<ThriftWithdraw> queryWithdrawHistorys(String depositCardId) throws TException {
        List<WithdrawHistory> withdrawHistory = withdrawHistoryMapper.queryWithdrawHistoryList(depositCardId);
        return withdrawHistory.stream().map(WithdrawHistory::toThrift).collect(Collectors.toList());
    }

}
