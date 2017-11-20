package com.icekredit.rpc.thrift.example.rest;

import com.icekredit.rpc.thrift.example.feign.ICalculatorService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(serviceId = "thrift-rest-calculator")
public interface CalculatorFeignClient extends ICalculatorService {

    @Override
    @GetMapping("/rest/add")
    int add(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception;

    @Override
    @GetMapping("/rest/subtract")
    int subtract(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception;

    @Override
    @GetMapping("/rest/multiply")
    int multiply(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception;

    @Override
    @GetMapping("/rest/division")
    int division(@RequestParam("arg1") int arg1, @RequestParam("arg2") int arg2) throws Exception;

}
