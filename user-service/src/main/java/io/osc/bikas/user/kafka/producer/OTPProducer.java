package io.osc.bikas.user.kafka.producer;

import com.osc.bikas.avro.OTPAvro;
import com.osc.bikas.avro.RegistrationUserAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPProducer {

    private final KafkaTemplate<String, OTPAvro> kafkaTemplate;

    private static final String TOPIC = "bikas-OTP-topic";

    public void sendMessage(String key, OTPAvro value) {
        kafkaTemplate.send(TOPIC, key, value);
        log.info("Published otp event [ key: {}, value:{} ]", key, value);
    }
}
