package io.osc.bikas.mail.consumer;

import com.osc.bikas.avro.OTPAvro;
import io.osc.bikas.mail.service.EmailSenderService;
import io.osc.bikas.mail.utils.EmailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OTPConsumer {

    private final EmailSenderService senderService;

    @KafkaListener(topics = "bikas-otp-topic", groupId = "message-service")
    public void consumeMessage(ConsumerRecord<String, OTPAvro> record) {
        var value = record.value();
        if(value != null) {
            if(value.getAttempts() == 0) {
                String emailText = EmailTemplate.generateOtpEmail(
                        value.getUserId().toString(),
                        value.getOtp()
                );
                senderService.sendEmail(
                        value.getEmail().toString(),
                        "Validation OTP",
                        emailText
                );
            }
        }
        log.info("record consumer :{}", record);
    }

}
