package com.giraone.queuing.worker.service.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PingServiceTest {

    @Autowired
    PingService pingService;

    @Test
    void assertThat_getString_works() {

        assertThat(pingService.getOkString()).isEqualTo("OK");
    }
}