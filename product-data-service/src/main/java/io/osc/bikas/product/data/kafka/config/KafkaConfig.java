package io.osc.bikas.product.data.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import io.osc.bikas.product.data.kafka.KafkaConstant;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaAdmin kafkaAdmin;

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder.name(KafkaConstant.USER_PRODUCT_VIEW_TOPIC).build();
    }

    @Bean
    public NewTopic categoriesTopic() {
        return TopicBuilder.name(KafkaConstant.CATEGORIES_TOPIC).build();
    }

}
