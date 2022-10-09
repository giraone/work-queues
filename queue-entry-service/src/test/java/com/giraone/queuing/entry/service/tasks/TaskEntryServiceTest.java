package com.giraone.queuing.entry.service.tasks;

import com.giraone.queuing.entry.service.tasks.dto.TaskRequest;
import com.giraone.queuing.entry.service.tasks.dto.TaskTicket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskEntryServiceTest {

    @Autowired
    TaskEntryService taskEntryService;

    @Test
    void addNewTask() {

        // arrange
        TaskRequest taskRequest = TaskRequest.builder()
            .type("TestType")
            .build();

        // act
        Mono<TaskTicket> fut = taskEntryService.addNewTaskToQueue(taskRequest);

        // act/assert
        StepVerifier.create(fut)
            .assertNext(responseEntity -> {
                assertThat(responseEntity.getTaskReferenceId()).isNotNull();
            })
            .verifyComplete();
    }
}