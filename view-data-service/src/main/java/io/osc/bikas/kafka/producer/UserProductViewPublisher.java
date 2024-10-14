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

    private final KafkaTemplate<String, UserProductViewTopicValue> kafkaTemplate;

    public void publish() {
        Random rand = new Random();
        String key = "userId";
        var value = UserProductViewTopicValue.newBuilder().setProductId(String.valueOf(rand.nextInt(5))).setViewedAt(LocalDateTime.now().toString()).build();
        kafkaTemplate.send(KafkaConst.USER_PRODUCT_VIEW_TOPIC, key, value);
    }

}
