package io.osc.bikas.product.data.kafka.config;

import com.osc.bikas.avro.ProductDetailsList;
import com.osc.bikas.avro.SortedProductDetails;
import com.osc.bikas.avro.SortedProductTopicKey;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;

import io.osc.bikas.product.data.kafka.KafkaConst;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public NewTopic ProductDataTopic() {
        return TopicBuilder.name(KafkaConst.PRODUCT_DATA_TOPIC)
                .build();
    }

    @Bean
    public NewTopic productClick() {
        return TopicBuilder.name(KafkaConst.PRODUCT_CLICK_TOPIC)
                .build();
    }


}
