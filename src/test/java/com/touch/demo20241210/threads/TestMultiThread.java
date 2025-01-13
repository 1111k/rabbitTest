package com.touch.demo20241210.threads;


import com.touch.service.UserService;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestMultiThread {

    @Autowired
    UserService userService;


    @Test
    public void startTest() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(200);
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                // 调用服务接口
                userService.add();
                countDownLatch.countDown();
            });
        }
        executor.awaitTermination(1000, TimeUnit.SECONDS);
        countDownLatch.await();
        executor.shutdown();
    }
}
