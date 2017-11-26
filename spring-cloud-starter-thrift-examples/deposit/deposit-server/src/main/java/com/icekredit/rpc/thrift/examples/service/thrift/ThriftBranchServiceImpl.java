package com.icekredit.rpc.thrift.examples.service.thrift;

import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import com.icekredit.rpc.thrift.examples.mapper.BranchMapper;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftBank;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftBranch;
import com.icekredit.rpc.thrift.examples.thrift.service.ThriftBranchService;
import com.icekredit.rpc.thrift.server.annotation.ThriftService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ThriftService(name = "thriftBranchService")
public class ThriftBranchServiceImpl implements ThriftBranchService.Iface {

    private final BranchMapper branchMapper;

    @Autowired
    public ThriftBranchServiceImpl(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    @Override
    @Transactional
    public void addNewBranch(long bankId, ThriftBranch branch) throws TException {
        Branch newBranch = Branch.fromThrift(branch);
        newBranch.setBankId(bankId);
        branchMapper.save(newBranch);
    }

    @Override
    public List<ThriftBranch> queryAllBranches(long bankId) throws TException {
        List<Branch> branches = branchMapper.findAll();
        return branches.stream().map(branch -> {
            ThriftBranch thriftBranch = branch.toThrift();

            ThriftBank thriftBank = new ThriftBank();
            thriftBank.setId(branch.getBankId());
            thriftBranch.setBank(thriftBank);

            return thriftBranch;
        }).collect(Collectors.toList());
    }

    @Override
    public ThriftBranch getBranchById(long branchId) throws TException {
        Branch branch = branchMapper.findById(branchId);
        ThriftBranch thriftBranch = branch.toThrift();

        ThriftBank thriftBank = new ThriftBank();
        thriftBank.setId(branch.getBankId());
        thriftBranch.setBank(thriftBank);

        return thriftBranch;
    }

}
