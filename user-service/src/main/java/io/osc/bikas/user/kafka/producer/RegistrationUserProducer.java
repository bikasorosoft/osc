package io.osc.bikas.user.kafka.producer;

import com.osc.bikas.avro.RegistrationUserAvro;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationUserProducer {

    @Autowired
    private KafkaTemplate<String, RegistrationUserAvro> kafkaTemplate;

    private static final String TOPIC = "bikas-registration-topic";

    public void sendMessage(String key, RegistrationUserAvro value) {
        kafkaTemplate.send(TOPIC, key, value);
        log.info("Published registration event [ key: {}, value: {} ]", key, value);
    }
}
