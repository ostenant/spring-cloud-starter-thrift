package io.ostenant.rpc.thrift.examples.service.http.service.impl;


import io.ostenant.rpc.thrift.examples.http.entities.Bank;
import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import io.ostenant.rpc.thrift.examples.http.enums.Region;
import io.ostenant.rpc.thrift.examples.mapper.BankMapper;
import io.ostenant.rpc.thrift.examples.mapper.BranchMapper;
import io.ostenant.rpc.thrift.examples.service.http.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {

    private final BankMapper bankMapper;
    private final BranchMapper branchMapper;

    @Autowired
    public BankServiceImpl(BankMapper bankMapper, BranchMapper branchMapper) {
        this.bankMapper = bankMapper;
        this.branchMapper = branchMapper;
    }

    @Override
    @Transactional
    public void registerNewBank(Bank bank) {
        bankMapper.save(bank);
    }

    @Override
    public List<Bank> queryAllBanks() {
        return bankMapper.findAll();
    }

    @Override
    public Bank getBankById(Long bankId) {
        return bankMapper.findById(bankId);
    }

    @Override
    public Map<Region, List<Branch>> queryAllBranchesByRegion(Long bankId) {
        List<Branch> branchList = branchMapper.queryAllBranchesByBankId(bankId);
        return branchList.stream().collect(
                Collectors.groupingBy(
                        branch -> Region.findByValue(branch.getRegionId()), Collectors.toList()));
    }

}
