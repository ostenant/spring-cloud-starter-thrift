package com.icekredit.rpc.thrift.examples.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController("restCalculatorService")
@RequestMapping("/rest")
public class RestCalculatorService {

    @GetMapping("/add")
    public int add(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.add(arg2Decimal).intValue();
    }

    @GetMapping("/subtract")
    public int subtract(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.subtract(arg2Decimal).intValue();
    }

    @GetMapping("/multiply")
    public int multiply(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.multiply(arg2Decimal).intValue();
    }

    @GetMapping("/division")
    public int division(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) {
        BigDecimal arg1Decimal = new BigDecimal(arg1);
        BigDecimal arg2Decimal = new BigDecimal(arg2);
        return arg1Decimal.divide(arg2Decimal).intValue();
    }
}
