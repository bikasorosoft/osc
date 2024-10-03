package io.osc.bikas.product.data.kafka.producer;

import com.osc.bikas.avro.Categories;
import com.osc.bikas.avro.CategoryDetails;
import io.osc.bikas.product.data.kafka.KafkaConstant;
import io.osc.bikas.product.data.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoriesPublisher {

    private final KafkaTemplate<String, Categories> kafkaTemplate;

    public void publish(Categories categories) {
        kafkaTemplate.send(KafkaConstant.CATEGORIES_TOPIC, "categories", categories);
    }

}
