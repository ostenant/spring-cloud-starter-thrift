package com.icekredit.rpc.thrift.examples.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ITestController {

    @GetMapping("/http/test")
    String test(@RequestParam("length") int length);

}
