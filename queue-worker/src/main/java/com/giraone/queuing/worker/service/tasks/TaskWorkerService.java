package com.giraone.queuing.worker.service.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giraone.queuing.worker.common.ObjectMapperBuilder;
import com.giraone.queuing.worker.config.ApplicationProperties;
import com.giraone.queuing.worker.service.tasks.dto.TaskRequest;
import com.giraone.queuing.worker.service.tasks.payload.FibonacciPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class TaskWorkerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWorkerService.class);

    private final ApplicationProperties applicationProperties;
    private final ObjectMapper objectMapper;
    private final int numberOfCores;

    public TaskWorkerService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.objectMapper = ObjectMapperBuilder.build(false, false);
        this.numberOfCores = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Number of cores={}", this.numberOfCores);
    }

    // @RabbitListener(queues = "#{application.consumer.getQueueName()}", concurrency = "#{numberOfCores}")
    @RabbitListener(queues = "QUEUE", concurrency = "8")
    public void receive(String payload, @Header("isoTimestamp") String isoTimestamp) {

        LocalDateTime now = LocalDateTime.now();
        long latency = calculateLatency(now, isoTimestamp);
        LOGGER.info("{} {} latency={} ms", now, isoTimestamp, latency);
        LOGGER.debug("payload={}", payload);
        TaskRequest taskRequest = null;
        try {
            taskRequest = objectMapper.readValue(payload, TaskRequest.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Cannot parse payload: {}", payload, e);
            return;
        }
        try {
            Map<String, Object> combinedResult = runTask(taskRequest.getType(), taskRequest.getParameter());
            LOGGER.info("combinedResult={}", combinedResult);
        } catch (Exception e) {
            LOGGER.error("Error running task: {} with input parameter {}", taskRequest.getType(), taskRequest.getParameter(), e);
        }
    }

    private Map<String, Object> runTask(String type, Map<String, Object> parameters) {

        if ("Fibonacci".equals(type)) {
            final long start = System.currentTimeMillis();
            Map<String, Object> result = new FibonacciPayload().run(parameters);
            final long end = System.currentTimeMillis();
            Map<String, Object> combinedResult = Map.of(
                "result", result,
                "_taskMs", end-start
            );
            return combinedResult;
        } else {
            LOGGER.warn("Unknown payload type \"{}\"! Valid is \"Fibonacci\".", type);
            return null;
        }
    }

    private long calculateLatency(LocalDateTime now, String sentIsoTimestamp) {

        if (sentIsoTimestamp != null) {
            LocalDateTime sent = null;
            try {
                sent = LocalDateTime.parse(sentIsoTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e) {
                LOGGER.error("Cannot parse \"{}\"", sentIsoTimestamp, e);
                return -2L;
            }
            return ChronoUnit.MILLIS.between(sent, now);
        } else {
            return -1L;
        }
    }
}
