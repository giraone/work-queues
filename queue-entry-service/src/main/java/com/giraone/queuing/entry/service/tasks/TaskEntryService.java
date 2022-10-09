package com.giraone.queuing.entry.service.tasks;

import com.giraone.queuing.entry.config.ApplicationProperties;
import com.giraone.queuing.entry.service.tasks.dto.TaskRequest;
import com.giraone.queuing.entry.service.tasks.dto.TaskTicket;
import net.mguenther.idem.flake.Flake64L;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TaskEntryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskEntryService.class);

    private final ApplicationProperties applicationProperties;
    private final RabbitTemplate rabbitTemplate;
    private final Flake64L flake64;

    public TaskEntryService(ApplicationProperties applicationProperties, RabbitTemplate rabbitTemplate) {
        this.applicationProperties = applicationProperties;
        this.rabbitTemplate = rabbitTemplate;

        final long pid = ProcessHandle.current().pid();
        this.flake64 = new Flake64L(
            new LinearTimeProvider(),
            new StaticWorkerIdProvider(Long.toHexString(pid)));
    }

    public Mono<TaskTicket> addNewTaskToQueue(TaskRequest taskRequest) {

        LOGGER.debug("addNewTaskToQueue: {}", taskRequest);

        final ApplicationProperties.AmqpProducerProperties amqpProducerProperties = applicationProperties.getProducer();
        return Mono.fromCallable(() -> {
            final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            rabbitTemplate.convertAndSend(
                amqpProducerProperties.getExchangeName(),
                amqpProducerProperties.getRoutingKey(),
                taskRequest,
                m -> {
                    // we do not want __TypeId__=com.giraone.queuing.entry.service.tasks.dtoTaskRequest in the header
                    m.getMessageProperties().getHeaders().remove("__TypeId__");
                    // but our own time stamp, sth. like "2022-10-09T22:35:38.6182613" in the header
                    m.getMessageProperties().setHeader("isoTimestamp", timestamp);
                    // a (hopefully) unique and k-ordered message id
                    m.getMessageProperties().setMessageId(generateMessageId());
                    return m;
                });
            return new TaskTicket(UUID.randomUUID().toString());
        });
    }

    private String generateMessageId() {

       return Long.toHexString(flake64.nextId());
    }
}
