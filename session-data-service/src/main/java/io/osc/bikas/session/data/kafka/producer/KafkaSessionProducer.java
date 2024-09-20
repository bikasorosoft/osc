package io.osc.bikas.session.data.kafka.producer;

import io.osc.bikas.session.data.kafka.config.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSessionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String userId, String deviceType, String sessionId) {
        String key = "{userId:" + userId + ",deviceType:" + deviceType + "}";
        String value = sessionId;

        kafkaTemplate.send(KafkaConstants.SESSION_TOPIC, key, value);
        log.info("message published to {} key: {}, value: {}", KafkaConstants.SESSION_TOPIC, key, value);
    }

}
