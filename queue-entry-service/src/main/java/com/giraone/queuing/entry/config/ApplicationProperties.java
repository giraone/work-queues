package com.giraone.queuing.entry.config;

import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import reactor.core.publisher.Hooks;

import javax.annotation.PostConstruct;

/**
 * Properties are configured in the {@code application-test.yml} file.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
// exclude from test coverage
@Generated
public class ApplicationProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);

    private static final int DEFAULT_RETRY_FIXED_DELAY_IN_MILLISECONDS = 500;
    private static final int RETRY_DEFAULT_NUMBER_OF_ATTEMPTS = 2;

    /**
     * Log the configuration to the log on startup
     */
    private boolean showConfigOnStartup = true;
    /**
     * WebFlux Hooks.onOperatorDebug() to get full stack traces. Should not be used in production.
     */
    private boolean debugHooks;
    /**
     * Enable reactor-tools ReactorDebugAgent to get stack traces. Can be used also in production.
     */
    private boolean debugAgent;
    /**
     * AMQP producer configuration properties
     */
    private AmqpProducerProperties producer = new AmqpProducerProperties();
    /**
     * AMQP consumer configuration properties
     */
    private AmqpConsumerProperties consumer = new AmqpConsumerProperties();

    @SuppressWarnings("squid:S2629") // invoke conditionally
    @PostConstruct
    private void startup() {
        if (this.showConfigOnStartup) {
            LOGGER.info(this.toString());
        }
        if (this.debugHooks) {
            LOGGER.warn("WEBFLUX DEBUG: Enabling Hooks.onOperatorDebug. DO NOT USE IN PRODUCTION!");
            Hooks.onOperatorDebug();
            if (this.debugAgent) {
                LOGGER.error("WEBFLUX DEBUG: DO NOT USE debug-hooks together with debug-agent!");
            }
        } else if (this.debugAgent) {
            long s = System.currentTimeMillis();
            LOGGER.info("WEBFLUX DEBUG: Enabling ReactorDebugAgent. Init may take 20 seconds! May slow down runtime performance (only) slightly.");
            // See: https://github.com/reactor/reactor-tools and https://github.com/reactor/reactor-core/tree/main/reactor-tools
            //            ReactorDebugAgent.init();
            //            ReactorDebugAgent.processExistingClasses();
            LOGGER.info("WEBFLUX DEBUG: ReactorDebugAgent.processExistingClasses finished in {} ms", System.currentTimeMillis() - s);
        }
    }

    @Data
    public static class AmqpProducerProperties {

        private String exchangeName = "entry";
        private String routingKey = "default";
    }

    @Data
    public static class AmqpConsumerProperties {

        private String queueName = "QUEUE";
        private String bindingKey = "default";
    }
}
