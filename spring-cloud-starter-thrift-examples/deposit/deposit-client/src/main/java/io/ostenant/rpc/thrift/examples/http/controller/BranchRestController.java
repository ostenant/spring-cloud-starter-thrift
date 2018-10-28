package io.ostenant.rpc.thrift.examples.http.controller;

import io.ostenant.rpc.thrift.examples.http.client.BranchFeignClient;
import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/branch")
public class BranchRestController {

    private final BranchFeignClient feignClient;

    @Autowired
    public BranchRestController(BranchFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @PostMapping("/addNewBranch")
    public void addNewBranch(@RequestParam("bankId") Branch branch) throws Exception {
        feignClient.addNewBranch(branch);
    }

    @GetMapping("/queryAllBranches")
    public List<Branch> queryBankBranches(@RequestParam("bankId") Long bankId) throws Exception {
        return feignClient.queryBankBranches(bankId);
    }

    @GetMapping("/getBranchById")
    public Branch getBranchById(@RequestParam("bankId") Long branchId) throws Exception {
        return feignClient.getBranchById(branchId);
    }


}
