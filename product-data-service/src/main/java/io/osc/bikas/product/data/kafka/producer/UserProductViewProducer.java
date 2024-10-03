package io.osc.bikas.product.data.kafka.producer;

import com.osc.bikas.avro.ProductAvro;
import io.osc.bikas.product.data.kafka.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProductViewProducer {

    private final KafkaTemplate<String, ProductAvro> kafkaTemplate;

    public void sendMessage(String key, ProductAvro value) {

        kafkaTemplate.send(KafkaConstant.USER_PRODUCT_VIEW_TOPIC, key, value);
        log.info("Published user product view event [ key: {}, value: {} ]", key, value);
    }

}
