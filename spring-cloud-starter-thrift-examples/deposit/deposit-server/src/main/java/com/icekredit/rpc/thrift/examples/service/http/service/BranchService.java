package com.icekredit.rpc.thrift.examples.service.http.service;


import com.icekredit.rpc.thrift.examples.http.entities.Branch;

import java.util.List;

public interface BranchService {

    void addNewBranch(Long bankId, Branch branch);

    List<Branch> queryAllBranches(Long bankId);

    Branch getBranchById(Long branchId);

}
