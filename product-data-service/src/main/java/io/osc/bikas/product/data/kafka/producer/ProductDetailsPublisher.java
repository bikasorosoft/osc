package io.osc.bikas.product.data.kafka.producer;

import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailsPublisher {

    private final KafkaTemplate<String, ProductDetails> kafkaTemplate;

    public void publish(String productId, Product product) {

        String key = productId;
        ProductDetails value = generateProductDetailsFrom(product);

        kafkaTemplate.send(KafkaConst.PRODUCT_DATA_TOPIC, key, value);

    }

    private ProductDetails generateProductDetailsFrom(Product product) {
        return ProductDetails.newBuilder()
                .setProductId(product.getProductId())
                .setCategoryId(product.getCategory().getCategoryId())
                .setProductName(product.getProductName())
                .setProductDescription(product.getProductDescription())
                .setProductPrice(product.getProductPrice().doubleValue())
                .setViewCount(product.getViewCount())
                .setImagePath(product.getImagePath())
                .build();
    }

}
