package io.ostenant.rpc.thrift.examples.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

@RestController
public class TestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestApiController.class);

    private static final AtomicLong REST_COUNTER = new AtomicLong(0);
    private static final AtomicLong RPC_COUNTER = new AtomicLong(0);

    private final ExecutorService executors = Executors.newFixedThreadPool(10);
    private final RestTemplate restTemplate;

    @Value("${client.ports}")
    private int[] clientPort;

    @Autowired
    public TestApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/api/rest", produces = "text/json;charset=UTF-8")
    public String testRest(@RequestParam("length") int length,
                           @RequestParam("times") int times,
                           @RequestParam("concurrency") int concurrency) throws Exception {
        final long counter = REST_COUNTER.getAndIncrement();
        final int port = clientPort[(int) (counter % clientPort.length)];

        LOGGER.info("The {} times to access '/api/rest', selected port is {}", counter, port);

        final String restUri = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(port)
                .path("/rest/test")
                .queryParam("length", length)
                .build().toUriString();

        CountDownLatch countDownLatch = new CountDownLatch(concurrency);

        final List<LongSummaryStatistics> summaryStatistics = new ArrayList<>();
        int perTime = times / concurrency;
        for (int i = 0; i < concurrency; i++) {
            executors.submit(() -> {
                LongSummaryStatistics statistics = LongStream.range(0, perTime)
                        .map(time -> restTemplate.getForObject(restUri, Long.class))
                        .summaryStatistics();
                summaryStatistics.add(statistics);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        JSONArray statisticsArray = new JSONArray();
        for (LongSummaryStatistics statistics : summaryStatistics) {
            JSONObject statisticsJSON = new JSONObject(true);
            statisticsJSON.put("1.Rest单次请求数据大小", length + "KB");
            statisticsJSON.put("2.Rest请求总次数", statistics.getCount() + "次");
            statisticsJSON.put("3.Rest请求总时间", statistics.getSum() + "ms");
            statisticsJSON.put("4.Rest单次请求最短时间", statistics.getMin() + "ms");
            statisticsJSON.put("5.Rest单次请求最长时间", statistics.getMax() + "ms");
            statisticsJSON.put("6.Rest单次请求平均时间", statistics.getAverage() + "ms");
            statisticsArray.add(statisticsJSON);
        }

        return statisticsArray.toJSONString();
    }


    @GetMapping(value = "/api/rpc", produces = "text/json;charset=UTF-8")
    public String testRpc(@RequestParam("length") int length,
                          @RequestParam("times") int times,
                          @RequestParam("concurrency") int concurrency) throws Exception {
        final long counter = RPC_COUNTER.getAndIncrement();
        final int port = clientPort[(int) (counter % clientPort.length)];

        LOGGER.info("The {} times to access '/api/rpc', selected port is {}", counter, port);

        final String rpcUri = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(port)
                .path("/rpc/test")
                .queryParam("length", length)
                .build().toUriString();

        CountDownLatch countDownLatch = new CountDownLatch(concurrency);

        final List<LongSummaryStatistics> summaryStatistics = new ArrayList<>();
        int perTime = times / concurrency;
        for (int i = 0; i < concurrency; i++) {
            executors.submit(() -> {
                LongSummaryStatistics statistics = LongStream.range(0, perTime)
                        .map(time -> restTemplate.getForObject(rpcUri, Long.class))
                        .summaryStatistics();
                summaryStatistics.add(statistics);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();


        JSONArray statisticsArray = new JSONArray();
        for (LongSummaryStatistics statistics : summaryStatistics) {
            JSONObject statisticsJSON = new JSONObject(true);
            statisticsJSON.put("1.Rpc单次请求数据大小", length + "KB");
            statisticsJSON.put("2.Rpc请求总次数", statistics.getCount() + "次");
            statisticsJSON.put("3.Rpc请求总时间", statistics.getSum() + "ms");
            statisticsJSON.put("4.Rpc单次请求最短时间", statistics.getMin() + "ms");
            statisticsJSON.put("5.Rpc单次请求最长时间", statistics.getMax() + "ms");
            statisticsJSON.put("6.Rpc单次请求平均时间", statistics.getAverage() + "ms");
            statisticsArray.add(statisticsJSON);
        }

        return statisticsArray.toJSONString();

    }
}
