package com.icekredit.rpc.thrift.examples.controller;

import com.icekredit.rpc.thrift.example.rest.CalculatorFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class RestCalculatorController {

    @Autowired
    CalculatorFeignClient calculatorService;

    @GetMapping("/add")
    public int add(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculatorService.add(arg1, arg2);
    }

    @GetMapping("/subtract")
    public int subtract(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculatorService.subtract(arg1, arg2);

    }

    @GetMapping("/multiply")
    public int multiply(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculatorService.multiply(arg1, arg2);
    }

    @GetMapping("/division")
    public int division(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception {
        return calculatorService.division(arg1, arg2);
    }

}
