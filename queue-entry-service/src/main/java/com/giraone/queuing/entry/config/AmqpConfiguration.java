package com.giraone.queuing.entry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giraone.queuing.entry.common.ObjectMapperBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        final ObjectMapper objectMapper = ObjectMapperBuilder.build(false, false);
        final Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
