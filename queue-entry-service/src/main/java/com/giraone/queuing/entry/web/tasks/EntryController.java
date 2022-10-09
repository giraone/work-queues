package com.giraone.queuing.entry.web.tasks;

import com.giraone.queuing.entry.service.tasks.TaskEntryService;
import com.giraone.queuing.entry.service.tasks.dto.TaskRequest;
import com.giraone.queuing.entry.service.tasks.dto.TaskTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/api/v1/tasks")
public class EntryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryController.class);

    private final TaskEntryService taskEntryService;

    public EntryController(TaskEntryService taskEntryService) {
        this.taskEntryService = taskEntryService;
    }

    @PostMapping
    public Mono<ResponseEntity<TaskTicket>> addNewTask(@RequestBody TaskRequest taskRequest) {

        LOGGER.debug("addNewTask: {}", taskRequest);
        return this.taskEntryService.addNewTaskToQueue(taskRequest)
            .map(ResponseEntity::ok);
    }
}
