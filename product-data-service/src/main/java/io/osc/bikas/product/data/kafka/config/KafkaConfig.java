package io.osc.bikas.product.data.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import io.osc.bikas.product.data.kafka.KafkaConst;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaAdmin kafkaAdmin;

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    @Bean
    public NewTopic productViewTopic() {
        return TopicBuilder.name(KafkaConst.PRODUCT_VIEW_TOPIC).build();
    }

    @Bean
    public NewTopic sortedCategoriesTopic() {
        return TopicBuilder.name(KafkaConst.SORTED_CATEGORIES_TOPIC)
                .build();
    }

    @Bean
    public NewTopic ProductDataTopic() {
        return TopicBuilder.name(KafkaConst.PRODUCT_DATA_TOPIC)
                .build();
    }

    @Bean
    public NewTopic sortedProductDataTopic() {
        return TopicBuilder.name(KafkaConst.SORTED_PRODUCT_DATA_TOPIC)
                .build();
    }

}
