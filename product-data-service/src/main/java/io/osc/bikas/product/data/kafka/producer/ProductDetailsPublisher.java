package io.osc.bikas.product.data.kafka.producer;

import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class ProductDetailsPublisher {

    private final KafkaTemplate<String, ProductDetails> kafkaTemplate;

    public void publish(String productId, Product product) {

        String key = productId;
        ProductDetails value = generateProductDetailsFrom(product);

        try {
            kafkaTemplate.send(KafkaConst.PRODUCT_DATA_TOPIC, key, value).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(String.format("unable to process the message key : %s, value: %s \n %s", key, value.toString(), e.getMessage()));
        }
    }

    private ProductDetails generateProductDetailsFrom(Product product) {
        return ProductDetails.newBuilder()
                .setProductName(product.getProductName())
                .setProductDescription(product.getProductDescription())
                .setProductPrice(product.getProductPrice().doubleValue())
                .build();
    }

}
