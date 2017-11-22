package com.icekredit.rpc.thrift.examples.service.http.service;


import com.icekredit.rpc.thrift.examples.http.entities.Bank;
import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import com.icekredit.rpc.thrift.examples.http.enums.Region;

import java.util.List;
import java.util.Map;

public interface BankService {

    void registerNewBank(Bank bank);

    List<Bank> queryAllBanks();

    Bank getBankById(Long bankId);

    Map<Region, List<Branch>> queryAllBranchesByRegion(Long bankId);

}
