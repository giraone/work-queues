package com.giraone.queuing.entry.web.system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Test class for the PingApiResource REST controller.
 * Basically only to show, how these kinds of reactive WebTestClient tests with "full application context" work.
 *
 * @see PingApiController
 */
@ExtendWith(SpringExtension.class) // for JUnit 5
@SpringBootTest()
@AutoConfigureWebTestClient
@SuppressWarnings("squid:S100") // Method naming
class PingApiControllerIntTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("Test GET /system-api/v1/ping (full context)")
    @Test
    void assertThat_ping_works() {

        // act/assert
        webTestClient.get().uri("/system-api/v1/ping")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON) // Normally not necessary
            .expectBody()
            .jsonPath("$.status").isNotEmpty()
            .jsonPath("$.status").isEqualTo("OK");
    }
}
