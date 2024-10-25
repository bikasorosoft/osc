package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailsInteractiveQuery {

    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;

    public ProductDto get(String productId) {

        var store = kafkaInteractiveQueryService.getProductDetailsReadOnlyKeyValueStore();
        ProductDetails productDetails = store.get(productId);

        return new ProductDto(productId, productDetails);

    }

}
