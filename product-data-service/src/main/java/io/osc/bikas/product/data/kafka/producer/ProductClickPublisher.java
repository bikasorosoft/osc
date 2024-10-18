package io.osc.bikas.product.data.kafka.producer;

import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClickPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String userId, String productId) {

        kafkaTemplate.send(KafkaConst.PRODUCT_CLICK_TOPIC, userId, productId);

    }

}
