package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.CategoryList;
import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.dto.CategoryDto;
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

    public Product get(String productId) {

        var store = kafkaInteractiveQueryService.getProductDetailsReadOnlyKeyValueStore();
        ProductDetails productDetails = store.get(productId);

        return generateProduct(productDetails);

    }

    private Product generateProduct(ProductDetails productDetails) {
        return Product.builder()
                .productId(productDetails.getProductId().toString())
                .category(Category.builder().categoryId(productDetails.getCategoryId().toString()).build())
                .productName(productDetails.getProductName().toString())
                .productPrice(BigDecimal.valueOf(productDetails.getProductPrice()))
                .productDescription(productDetails.getProductDescription().toString())
                .viewCount(productDetails.getViewCount())
                .imagePath(productDetails.getImagePath().toString())
                .build();
    }

}
