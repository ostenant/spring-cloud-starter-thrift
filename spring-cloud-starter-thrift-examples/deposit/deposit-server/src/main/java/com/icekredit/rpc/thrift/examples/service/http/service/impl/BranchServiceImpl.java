package com.icekredit.rpc.thrift.examples.service.http.service.impl;

import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import com.icekredit.rpc.thrift.examples.mapper.BranchMapper;
import com.icekredit.rpc.thrift.examples.service.http.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;

    @Autowired
    public BranchServiceImpl(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    @Override
    @Transactional
    public void addNewBranch(Long bankId, Branch branch) {
        branch.setBankId(bankId);
        branchMapper.save(branch);
    }

    @Override
    public List<Branch> queryAllBranches(Long bankId) {
        return branchMapper.queryAllBranchesByBankId(bankId);
    }

    @Override
    public Branch getBranchById(Long branchId) {
        return branchMapper.findById(branchId);
    }

}
