package com.giraone.queuing.entry.web.system;

import com.giraone.queuing.entry.config.AmqpConfiguration;
import com.giraone.queuing.entry.service.system.PingService;
import com.giraone.queuing.entry.service.tasks.SetupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

/**
 * Test class for the PingApiResource REST controller.
 * Basically only to show, how these kind of reactive WebTestClient/WebFluxTest tests without
 * "full application context" (only web layer, no full @SpringBootTest) work.
 *
 * @see PingApiController
 */
// the controller we want to test
@WebFluxTest(controllers = PingApiController.class)
// Classes, we need to import as dependencies in the "partial" application context.
// Has to be adapted each time, the IOC dependency for the startup is changed.
@Import({
    SetupService.class, AmqpConfiguration.class
})
class PingApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PingService pingService;

    @DisplayName("Test GET /system-api/v1/ping (web layer with mock)")
    @Test
    void assertThat_ping_works() {

        when(pingService.getOkString()).thenReturn("OK");

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
