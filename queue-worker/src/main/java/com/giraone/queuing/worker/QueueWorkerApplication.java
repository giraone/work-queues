package com.giraone.queuing.worker;

import com.giraone.queuing.worker.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class QueueWorkerApplication {

    private final ApplicationProperties applicationProperties;

    public QueueWorkerApplication(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public static void main(String[] args) {
		SpringApplication.run(QueueWorkerApplication.class, args);
	}
}
