package com.icekredit.rpc.thrift.examples.http.controller;

import com.icekredit.rpc.thrift.examples.http.client.TestFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    private final TestFeignClient feignClient;

    @Autowired
    public RestTestController(TestFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @GetMapping("/rest/test")
    public long test(@RequestParam("length") int length) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String result = feignClient.test(length);
        System.out.println(result.getBytes().length);

        stopWatch.stop();
        return stopWatch.getTotalTimeMillis();
    }


}
