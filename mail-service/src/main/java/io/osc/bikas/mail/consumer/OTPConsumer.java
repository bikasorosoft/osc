package io.osc.bikas.mail.consumer;

import com.osc.bikas.avro.OtpDetails;
import io.osc.bikas.mail.service.EmailSenderService;
import io.osc.bikas.mail.utils.EmailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OTPConsumer {

    private final EmailSenderService senderService;

    @KafkaListener(topics = "bikas-otp-topic", groupId = "message-service")
    public void consume(ConsumerRecord<String, OtpDetails> record) {
        var key = record.key();
        var value = record.value();
        String body = switch (value.getFormat()) {
            case FORGOT_PASSWORD -> {
                yield EmailTemplate.generateOtpEmail(value.getOtp());
            }
            case REGISTRATION -> {
                yield EmailTemplate.generateOtpEmail(key, value.getOtp());
            }
        };
        senderService.sendEmail(value.getEmail().toString(), "Reset OTP", body);
        log.info("record consumer :{}", record);
    }

}
