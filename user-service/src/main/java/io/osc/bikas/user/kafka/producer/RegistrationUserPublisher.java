package io.osc.bikas.user.kafka.producer;

import com.osc.bikas.avro.UserRegistrationDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationUserPublisher {

    @Autowired
    private KafkaTemplate<String, UserRegistrationDetail> kafkaTemplate;

    private static final String TOPIC = "bikas-registration-topic";

    public void publish(String key, UserRegistrationDetail value) {
        kafkaTemplate.send(TOPIC, key, value);
        log.info("Published registration event [ key: {}, value: {} ]", key, value);
    }
}
