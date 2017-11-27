package com.icekredit.rpc.thrift.examples.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

@RestController
public class TestApiController {

    private final RestTemplate restTemplate;

    @Autowired
    public TestApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/api/rest", produces = "text/json;charset=UTF-8")
    public String testRest(@RequestParam("length") int length,
                           @RequestParam("times") int times) {
        final String restUri = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(8080)
                .path("/rest/test")
                .queryParam("length", length)
                .build().toUriString();

        LongSummaryStatistics statistics = LongStream.range(0, times)
                .map(time -> restTemplate.getForObject(restUri, Long.class))
                .summaryStatistics();

        JSONObject statisticsJSON = new JSONObject(true);
        statisticsJSON.put("1.Rest单次请求数据大小", length + "KB");
        statisticsJSON.put("2.Rest请求总次数", statistics.getCount() + "次");
        statisticsJSON.put("3.Rest请求总时间", statistics.getSum() + "ms");
        statisticsJSON.put("4.Rest单次请求最短时间", statistics.getMin() + "ms");
        statisticsJSON.put("5.Rest单次请求最长时间", statistics.getMax() + "ms");
        statisticsJSON.put("6.Rest单次请求平均时间", statistics.getAverage() + "ms");

        return statisticsJSON.toJSONString();
    }


    @GetMapping(value = "/api/rpc", produces = "text/json;charset=UTF-8")
    public String testRpc(@RequestParam("length") int length,
                          @RequestParam("times") int times) {
        final String rpcUri = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(8080)
                .path("/rpc/test")
                .queryParam("length", length)
                .build().toUriString();

        LongSummaryStatistics statistics = LongStream.range(0, times)
                .map(time -> restTemplate.getForObject(rpcUri, Long.class))
                .summaryStatistics();

        JSONObject statisticsJSON = new JSONObject(true);
        statisticsJSON.put("1.Rpc单次请求数据大小", length + "KB");
        statisticsJSON.put("2.Rpc请求总次数", statistics.getCount() + "次");
        statisticsJSON.put("3.Rpc请求总时间", statistics.getSum() + "ms");
        statisticsJSON.put("4.Rpc单次请求最短时间", statistics.getMin() + "ms");
        statisticsJSON.put("5.Rpc单次请求最长时间", statistics.getMax() + "ms");
        statisticsJSON.put("6.Rpc单次请求平均时间", statistics.getAverage() + "ms");

        return statisticsJSON.toJSONString();
    }


}
