package io.osc.bikas.product.data.kafka.producer;

import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class ProductClickPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String userId, String productId) {

        try {
            kafkaTemplate.send(KafkaConst.PRODUCT_CLICK_TOPIC, userId, productId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(String.format("unable to process the message key : %s, value: %s \n %s", userId, productId, e.getMessage()));
        }

    }

}
