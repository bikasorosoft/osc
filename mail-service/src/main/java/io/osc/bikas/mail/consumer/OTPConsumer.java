package io.osc.bikas.mail.consumer;

import io.osc.bikas.mail.service.EmailSenderService;
import io.osc.bikas.mail.utils.EmailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OTPConsumer {

    private final EmailSenderService senderService;

    @KafkaListener(topics = "bikas-OTP-topic", groupId = "message-service")
    public void consumeMessage(GenericRecord record) {
        if(Integer.valueOf(record.get("Attempts").toString()) == 0) {
            String emailText = EmailTemplate.generateOtpEmail(
                    record.get("userId").toString(),
                    record.get("OTP").toString()
            );
            senderService.sendEmail(
                    record.get("email").toString(),
                    "Validation OTP",
                    emailText
            );
        }
        log.info("record consumer :{}", record);
    }

}
