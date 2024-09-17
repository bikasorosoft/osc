package io.osc.bikas.user.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder.name("registration-topic").build();
    }

    @Bean
    public NewTopic OTPTopic() {
        return TopicBuilder.name("OTP-topic").build();
    }


}
