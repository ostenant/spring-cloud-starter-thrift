package com.icekredit.rpc.thrift.examples.controller;

import com.icekredit.rpc.thrift.client.annotation.ThriftReferer;
import com.icekredit.rpc.thrift.examples.rpc.CalculatorThriftClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc")
public class RpcCalculatorController {

    @ThriftReferer
    CalculatorThriftClient calculators;

    @GetMapping("/add")
    public int add(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().add(arg1, arg2);
    }

    @GetMapping("/subtract")
    public int subtract(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().subtract(arg1, arg2);

    }

    @GetMapping("/multiply")
    public int multiply(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().multiply(arg1, arg2);
    }

    @GetMapping("/division")
    public int division(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculators.thriftClient().division(arg1, arg2);
    }

}
