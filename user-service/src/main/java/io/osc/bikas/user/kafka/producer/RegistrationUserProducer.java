package io.osc.bikas.user.kafka.producer;

import com.osc.bikas.avro.RegistrationUserAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegistrationUserProducer {

    @Autowired
    private KafkaTemplate<String, RegistrationUserAvro> kafkaTemplate;

    private static final String TOPIC = "registration-topic ";

    public void sendMessage(RegistrationUserAvro user) {
        kafkaTemplate.send(TOPIC, user.getUserId().toString(), user);
        System.out.println("Published user message: " + user);
    }
}
