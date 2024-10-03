package io.osc.bikas.user.kafka.producer;

import com.osc.bikas.avro.OtpDetails;
import io.osc.bikas.user.kafka.config.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpPublisher {

    private final KafkaTemplate<String, OtpDetails> kafkaTemplate;

    private static final String TOPIC = KafkaConstants.OTP_TOPIC;

    public void publish(String key, OtpDetails value) {
        kafkaTemplate.send(TOPIC, key, value);
        log.info("Published otp event [ key: {}, value:{} ]", key, value);
    }
}
