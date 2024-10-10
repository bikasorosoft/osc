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

    public void publish() {
        Random random = new Random();

        int randomInt = random.nextInt(3);

        kafkaTemplate.send(KafkaConst.PRODUCT_VIEW_TOPIC, String.valueOf(randomInt), String.valueOf(randomInt));

    }

}
