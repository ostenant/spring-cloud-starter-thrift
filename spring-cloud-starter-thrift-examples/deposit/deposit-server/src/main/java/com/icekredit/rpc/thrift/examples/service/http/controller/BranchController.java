package com.icekredit.rpc.thrift.examples.service.http.controller;

import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import com.icekredit.rpc.thrift.examples.service.http.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/http/branch")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/addNewBranch")
    public void addNewBranch(Branch branch) {
        branchService.addNewBranch(branch.getBankId(), branch);
    }

    @GetMapping("/queryAllBranches")
    public List<Branch> queryBankBranches(@RequestParam("bankId") Long bankId) {
        return branchService.queryAllBranches(bankId);
    }

    @GetMapping("/getBranchById")
    public Branch getBranchById(@RequestParam("bankId") Long branchId) {
        return branchService.getBranchById(branchId);
    }

}
