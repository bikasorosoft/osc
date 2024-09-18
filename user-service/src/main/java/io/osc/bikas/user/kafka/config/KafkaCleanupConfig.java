package io.osc.bikas.user.kafka.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaCleanupConfig {

    private final AdminClient adminClient;

    @PreDestroy
    public void cleanUpTopic() {
        try {
            adminClient.deleteTopics(
                    Set.of("bikas-registration-topic", "bikas-OTP-topic")
            ).all().get();
            log.info("topic deleted successfully!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
