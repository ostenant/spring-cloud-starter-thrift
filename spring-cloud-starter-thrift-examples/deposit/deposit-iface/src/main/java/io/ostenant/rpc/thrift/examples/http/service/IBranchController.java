package io.ostenant.rpc.thrift.examples.http.service;


import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/http/branch")
public interface IBranchController {

    @PostMapping("/addNewBranch")
    void addNewBranch(@RequestParam("bankId") Branch branch) throws Exception;

    @GetMapping("/queryAllBranches")
    List<Branch> queryBankBranches(@RequestParam("bankId") Long bankId) throws Exception;

    @GetMapping("/getBranchById")
    Branch getBranchById(@RequestParam("bankId") Long branchId) throws Exception;

}
