package io.ostenant.rpc.thrift.examples.thrift.controller;

import io.ostenant.rpc.thrift.client.annotation.ThriftRefer;
import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import io.ostenant.rpc.thrift.examples.thrift.client.BranchThriftClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rpc/branch")
public class BranchRpcController {

    @ThriftRefer
    private BranchThriftClient thriftClient;

    @PostMapping("/addNewBranch")
    public void addNewBranch(@RequestParam("bankId") Branch branch) throws Exception {
        thriftClient.client().addNewBranch(branch.getBankId(), branch.toThrift());
    }

    @GetMapping("/queryAllBranches")
    public List<Branch> queryBankBranches(@RequestParam("bankId") Long bankId) throws Exception {
        return thriftClient.client().queryAllBranches(bankId)
                .stream().map(Branch::fromThrift)
                .collect(Collectors.toList());
    }

    @GetMapping("/getBranchById")
    public Branch getBranchById(@RequestParam("bankId") Long branchId) throws Exception {
        return Branch.fromThrift(thriftClient.client().getBranchById(branchId));
    }

}
