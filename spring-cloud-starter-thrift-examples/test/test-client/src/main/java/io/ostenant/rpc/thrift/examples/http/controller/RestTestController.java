package io.ostenant.rpc.thrift.examples.http.controller;

import com.alibaba.fastjson.JSONObject;
import io.ostenant.rpc.thrift.examples.http.client.TestFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

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
        System.out.println(result);

        stopWatch.stop();
        return stopWatch.getTotalTimeMillis();
    }

    @GetMapping(value = "/rest/concurrency", produces = "text/json;charset=UTF-8")
    public String testRest(@RequestParam("length") int length,
                           @RequestParam("times") int times,
                           @RequestParam(value = "concurrency", required = false) int concurrency) throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(concurrency);
        final Semaphore semaphore = new Semaphore(100);

        final List<LongSummaryStatistics> summaryStatistics = new ArrayList<>();
        int perRequest = times / concurrency;

        ExecutorService executors;
        if (concurrency <= 0) {
            executors = Executors.newFixedThreadPool(1);
        } else if (concurrency < 1000) {
            executors = Executors.newFixedThreadPool(concurrency);
        } else {
            executors = Executors.newFixedThreadPool(1000);
        }

        AtomicInteger check = new AtomicInteger();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < concurrency; i++) {
            executors.submit(() -> {
                LongSummaryStatistics statistics = LongStream.range(0, perRequest)
                        .map(time -> {
                            String result = null;
                            StopWatch watch = new StopWatch();
                            try {
                                watch.start();
                                result = feignClient.test(length);
                                watch.stop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                System.out.println("Request time millis is: " + watch.getTotalTimeMillis() + "ms , result is: " + result);
                            }
                            return watch.getTotalTimeMillis();
                        }).summaryStatistics();


                synchronized (summaryStatistics) {
                    summaryStatistics.add(statistics);
                    System.out.println("RPC Call " + check.incrementAndGet() + ": " + Thread.currentThread().getName() + " ==> " + summaryStatistics);
                }

                countDownLatch.countDown();
                semaphore.release();
            });
        }

        long totalTimeMillis;

        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            totalTimeMillis = stopWatch.getTotalTimeMillis();
            executors.shutdown();
        }

        JSONObject restResult = summaryStatistics.stream().map(statistics -> {
            JSONObject s = new JSONObject();
            s.put("average", statistics.getAverage());
            s.put("min", statistics.getMin());
            s.put("max", statistics.getMax());
            s.put("total", statistics.getCount());
            return s;
        }).reduce((s1, s2) -> {
            JSONObject s3 = new JSONObject();
            s3.put("average", (s1.getDoubleValue("average") + s2.getDoubleValue("average")) / 2.0);
            s3.put("min", Math.min(s1.getLongValue("min"), s2.getLongValue("min")));
            s3.put("max", Math.max(s1.getLongValue("max"), s2.getLongValue("max")));
            s3.put("total", Math.addExact(s1.getLongValue("total"), s2.getLongValue("total")));
            return s3;
        }).orElseGet(JSONObject::new);

        if (restResult.isEmpty()) {
            return restResult.toJSONString();
        }

        JSONObject wrapperResult = new JSONObject();
        wrapperResult.put("REST单次请求数据大小", length + "KB");
        wrapperResult.put("REST请求总次数", restResult.getLongValue("total") + "次");
        wrapperResult.put("REST请求总时间", totalTimeMillis + "ms");
        wrapperResult.put("REST单次请求最短时间", restResult.getLongValue("min") + "ms");
        wrapperResult.put("REST单次请求最长时间", restResult.getLongValue("max") + "ms");
        wrapperResult.put("REST单次请求平均时间", restResult.getDoubleValue("average") + "ms");
        wrapperResult.put("REST每秒处理查询数", times / (totalTimeMillis / 1000.0));

        return wrapperResult.toJSONString();
    }

}
