package com.giraone.queuing.entry.web.tasks;

import com.giraone.queuing.entry.service.tasks.dto.TaskRequest;
import com.giraone.queuing.entry.service.tasks.dto.TaskTicket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "30000") // 30 seconds
class EntryControllerIntTest {

    protected static final String URL_PATH = "/api/v1/tasks";

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    void addNewTask() {

        // arrange
        TaskRequest taskRequest = TaskRequest.builder()
            .type("TestType")
            .build();

        // act
        TaskTicket result = webTestClient
            .post()
            .uri(URL_PATH)
            .body(BodyInserters.fromValue(taskRequest))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON) // Normally not necessary
            .expectBody(TaskTicket.class)
            .returnResult()
            .getResponseBody();

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getTaskReferenceId()).isNotNull();
    }
}