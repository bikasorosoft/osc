package io.osc.bikas.product.data.kafka.producer;

import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProductViewPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String key, String value) {

        kafkaTemplate.send(KafkaConst.PRODUCT_VIEW_TOPIC, key, value);

    }

}
