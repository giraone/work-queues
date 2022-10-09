package com.giraone.queuing.entry.service.tasks;

import com.giraone.queuing.entry.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class SetupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupService.class);

    private final ApplicationProperties applicationProperties;
    private final RabbitTemplate rabbitTemplate;

    public SetupService(ApplicationProperties applicationProperties, RabbitTemplate rabbitTemplate) {
        this.applicationProperties = applicationProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void setupQueueDestinations() {


        LOGGER.info("Creating RabbitMQ destination bindings for producer={}, consumer={}",
            applicationProperties.getProducer(), applicationProperties.getConsumer());
        final ApplicationProperties.AmqpProducerProperties amqpProducerProperties = applicationProperties.getProducer();
        final ApplicationProperties.AmqpConsumerProperties amqpConsumerProperties = applicationProperties.getConsumer();

        Exchange exchange = ExchangeBuilder.directExchange(amqpProducerProperties.getExchangeName())
            .durable(true)
            .build();

        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);

        rabbitAdmin.declareExchange(exchange);

        Queue queue = QueueBuilder.durable(amqpConsumerProperties.getQueueName()).build();

        rabbitAdmin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue)
            .to(exchange)
            .with(amqpConsumerProperties.getBindingKey())
            .noargs();

        rabbitAdmin.declareBinding(binding);

        LOGGER.info("RabbitMQ binding successfully created.");
    }
}
