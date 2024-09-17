package io.osc.bikas.user.kafka.producer;

import com.osc.bikas.avro.OTPAvro;
import com.osc.bikas.avro.RegistrationUserAvro;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPProducer {

    private final KafkaTemplate<String, OTPAvro> kafkaTemplate;

    private static final String TOPIC = "OTP-topic";

    public void sendMessage(OTPAvro otp) {
        kafkaTemplate.send(TOPIC, otp.getUserId().toString(), otp);
        System.out.println("Published otp message: " + otp);
    }
}
