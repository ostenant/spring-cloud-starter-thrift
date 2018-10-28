package io.ostenant.rpc.thrift.examples.thrift.controller;

import io.ostenant.rpc.thrift.client.annotation.ThriftRefer;
import io.ostenant.rpc.thrift.examples.http.entities.Bank;
import io.ostenant.rpc.thrift.examples.http.entities.Branch;
import io.ostenant.rpc.thrift.examples.http.enums.Region;
import io.ostenant.rpc.thrift.examples.thrift.client.BankThriftClient;
import io.ostenant.rpc.thrift.examples.thrift.entities.ThriftBranch;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftRegion;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rpc/bank")
public class BankRpcController {

    @ThriftRefer
    private BankThriftClient thriftClient;

    @PostMapping("/addNewBank")
    public void addNewBank(Bank bank) throws Exception {
        thriftClient.client().registerNewBank(bank.toThrift());
    }

    @GetMapping("/getBankById")
    public Bank getBankById(@RequestParam("bankId") Long bankId) throws Exception {
        return Bank.fromThrift(thriftClient.client().getBankById(bankId));
    }

    @GetMapping("/queryAllBranchesByRegion")
    public Map<Region, List<Branch>> queryAllBranchesByRegion(@RequestParam("bankId") Long bankId) throws Exception {
        Map<ThriftRegion, List<ThriftBranch>> thriftRegionListMap = thriftClient.client()
                .queryAllBranchesByRegion(bankId);
        Map<Region, List<Branch>> regionListMap = new HashMap<>();

        for (Map.Entry<ThriftRegion, List<ThriftBranch>> entry : thriftRegionListMap.entrySet()) {
            ThriftRegion thriftRegion = entry.getKey();
            Region region = Region.findByValue(thriftRegion.getValue());

            List<ThriftBranch> thriftBranches = entry.getValue();
            List<Branch> branchList = thriftBranches.stream().map(Branch::fromThrift).collect(Collectors.toList());
            regionListMap.put(region, branchList);
        }

        return regionListMap;
    }

}
