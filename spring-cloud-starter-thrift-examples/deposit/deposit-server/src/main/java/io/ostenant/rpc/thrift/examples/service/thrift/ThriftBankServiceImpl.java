package io.ostenant.rpc.thrift.examples.service.thrift;

import io.ostenant.rpc.thrift.examples.http.entities.Bank;
import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import io.ostenant.rpc.thrift.examples.mapper.BankMapper;
import io.ostenant.rpc.thrift.examples.mapper.BranchMapper;
import io.ostenant.rpc.thrift.examples.thrift.entities.ThriftBank;
import io.ostenant.rpc.thrift.examples.thrift.entities.ThriftBranch;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftRegion;
import io.ostenant.rpc.thrift.examples.thrift.service.ThriftBankService;
import io.ostenant.rpc.thrift.server.annotation.ThriftService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@ThriftService(name = "thriftBankService")
public class ThriftBankServiceImpl implements ThriftBankService.Iface {

    private final BankMapper bankMapper;
    private final BranchMapper branchMapper;

    @Autowired
    public ThriftBankServiceImpl(BankMapper bankMapper, BranchMapper branchMapper) {
        this.bankMapper = bankMapper;
        this.branchMapper = branchMapper;
    }

    @Override
    @Transactional
    public void registerNewBank(ThriftBank bank) throws TException {
        Bank newBank = Bank.fromThrift(bank);
        bankMapper.save(newBank);
    }

    @Override
    public List<ThriftBank> queryAllBanks() throws TException {
        List<Bank> queriedBankList = bankMapper.findAll();
        if (CollectionUtils.isEmpty(queriedBankList)) {
            return Collections.emptyList();
        }

        return queriedBankList.stream().map(Bank::toThrift).collect(Collectors.toList());
    }

    @Override
    public ThriftBank getBankById(long bankId) throws TException {
        Bank bank = bankMapper.findById(bankId);
        if (Objects.isNull(bank)) {
            return null;
        }

        return bank.toThrift();
    }

    @Override
    public Map<ThriftRegion, List<ThriftBranch>> queryAllBranchesByRegion(long bankId) throws TException {
        List<Branch> branchList = branchMapper.queryAllBranchesByBankId(bankId);

        return branchList.stream()
                .map(Branch::toThrift)
                .collect(Collectors.groupingBy(
                        thriftBranch -> ThriftRegion.findByValue(thriftBranch.getRegion().getValue()),
                        Collectors.toList()));
    }
}
