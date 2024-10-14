package io.osc.bikas.cart.data.kafka.config;

import io.osc.bikas.cart.data.kafka.KafkaConst;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic cartTopic() {
        return TopicBuilder.name(KafkaConst.CART_TOPIC).build();
    }

}
