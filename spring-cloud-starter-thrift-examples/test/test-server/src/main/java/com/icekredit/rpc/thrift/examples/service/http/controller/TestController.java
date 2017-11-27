package com.icekredit.rpc.thrift.examples.service.http.controller;

import com.icekredit.rpc.thrift.examples.http.ITestController;
import com.icekredit.rpc.thrift.examples.service.http.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements ITestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/http/test")
    public String test(@RequestParam("length") int length) {
        return testService.test(length);
    }

}
