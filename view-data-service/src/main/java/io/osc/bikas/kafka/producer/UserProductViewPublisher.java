package io.osc.bikas.kafka.producer;

import com.osc.bikas.avro.UserProductViewTopicValue;
import io.osc.bikas.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class UserProductViewPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String userId, String productId) {
        kafkaTemplate.send(KafkaConst.USER_PRODUCT_VIEW_TOPIC, userId, productId);
    }

}
