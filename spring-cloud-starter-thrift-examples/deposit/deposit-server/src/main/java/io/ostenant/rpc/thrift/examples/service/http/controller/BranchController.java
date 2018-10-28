package io.ostenant.rpc.thrift.examples.service.http.controller;

import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import io.ostenant.rpc.thrift.examples.http.service.IBranchController;
import io.ostenant.rpc.thrift.examples.service.http.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/http/branch")
public class BranchController implements IBranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/addNewBranch")
    public void addNewBranch(Branch branch) throws Exception {
        branchService.addNewBranch(branch.getBankId(), branch);
    }

    @GetMapping("/queryAllBranches")
    public List<Branch> queryBankBranches(@RequestParam("bankId") Long bankId) throws Exception {
        return branchService.queryAllBranches(bankId);
    }

    @GetMapping("/getBranchById")
    public Branch getBranchById(@RequestParam("bankId") Long branchId) throws Exception {
        return branchService.getBranchById(branchId);
    }

}
