package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.CategoryList;
import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.dto.ProductDto;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailsInteractiveQuery {

    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;

    public ProductDto get(String productId) {

        var store = kafkaInteractiveQueryService.getProductDetailsReadOnlyKeyValueStore();
        ProductDetails productDetails = store.get(productId);

        return generateProduct(productId, productDetails);

    }

    private ProductDto generateProduct(String productId, ProductDetails productDetails) {
        return ProductDto.builder().productName(productId)
                .categoryId(productId.substring(0,1))
                .productName(productDetails.getProductName().toString())
                .productPrice(productDetails.getProductPrice())
                .productDescription(productDetails.getProductDescription().toString())
                .build();

    }

}
