package com.giraone.queuing.entry;

import com.giraone.queuing.entry.config.ApplicationProperties;
import com.giraone.queuing.entry.service.tasks.SetupService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class QueueEntryServiceApplication {

    private final SetupService setupService;

    public QueueEntryServiceApplication(SetupService setupService) {
        this.setupService = setupService;
    }

    @PostConstruct
    public void initApplication() {

        setupService.setupQueueDestinations();
    }

    public static void main(String[] args) {
        SpringApplication.run(QueueEntryServiceApplication.class, args);
    }
}
