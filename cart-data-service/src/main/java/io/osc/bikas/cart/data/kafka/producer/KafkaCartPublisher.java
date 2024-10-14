package io.osc.bikas.cart.data.kafka.producer;

import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaCartPublisher {

    private final KafkaTemplate<String, CartItemList> kafkaTemplate;

    public void publish(String key, CartItemList value) {

        kafkaTemplate.send(KafkaConst.CART_TOPIC, key, value);

    }

}
