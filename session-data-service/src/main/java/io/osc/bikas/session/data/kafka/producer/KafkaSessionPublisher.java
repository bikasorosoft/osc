package io.osc.bikas.session.data.kafka.producer;

import com.osc.bikas.avro.SessionTopicKey;
import io.osc.bikas.session.data.kafka.config.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSessionPublisher {

    private final KafkaTemplate<SessionTopicKey, CharSequence> kafkaTemplate;

    public void publish(String userId, String deviceType, String sessionId) {
        SessionTopicKey key = SessionTopicKey.newBuilder()
                .setUserId(userId).setDevice(deviceType).build();
        CharSequence value = sessionId;

        kafkaTemplate.send(KafkaConstants.SESSION_TOPIC, key, value);
        log.info("message published to {} key: {}, value: {}", KafkaConstants.SESSION_TOPIC, key, value);
    }

}
