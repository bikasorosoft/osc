package io.osc.bikas.product.data.kafka.producer;

import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryDetailsPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String categoryId, String name) {
        kafkaTemplate.send(KafkaConst.CATEGORY_DATA_TOPIC, categoryId, name);
    }
}
