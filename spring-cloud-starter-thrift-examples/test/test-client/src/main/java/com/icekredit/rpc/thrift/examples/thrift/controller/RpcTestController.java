package com.icekredit.rpc.thrift.examples.thrift.controller;

import com.icekredit.rpc.thrift.client.annotation.ThriftReferer;
import com.icekredit.rpc.thrift.examples.thrift.client.TestThriftClient;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RpcTestController {

    @ThriftReferer
    TestThriftClient thriftClient;

    @GetMapping("/rpc/test")
    public long test(@RequestParam("length") int length) throws Exception {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        thriftClient.thriftClient().test(length);

        stopWatch.stop();

        return stopWatch.getTotalTimeMillis();
    }

}
