package io.osc.bikas.product.data.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.kafka.producer.ProductDetailsPublisher;
import io.osc.bikas.product.data.kafka.producer.ProductViewPublisher;
import io.osc.bikas.product.data.kafka.service.KafkaInteractiveQueryService;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductDataService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductViewPublisher productViewPublisher;
    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;
    private final ProductDetailsPublisher productDetailsPublisher;

    public List<Product> findFeaturedProducts() {
        return productRepository.findProductOrderedByProductViewCount();
    }

    public List<CategoryDto> findCategoriesOrderedByProductViewCount() {
        return productRepository.findCategoriesOrderedByProductViewCount();
    }

    public List<Product> filterProduct(String categoryId, CategoryFilterRequest.FILTER filter) {

        if (Objects.isNull(categoryId) || categoryId.isEmpty()) {
            return findFeaturedProducts();
        }

        if (Objects.isNull(filter)) {
            throw new IllegalArgumentException("Filter cannot be null");
        }

        return switch (filter) {
            case POPULAR -> productRepository.findByCategoryIdOrderByViewCountDesc(categoryId);
            case LOW_TO_HIGH -> productRepository.findByCategoryIdOrderByProductPriceAsc(categoryId);
            case HIGH_TO_LOW -> productRepository.findByCategoryIdOrderByProductPriceDesc(categoryId);
            case NEW_FIRST -> productRepository.findByCategoryIdOrderByProductId(categoryId);
            default -> throw new IllegalArgumentException("Unsupported filter type");
        };
    }

    public Product findProductById(String productId) {
        var store = kafkaInteractiveQueryService.getProductDetailsReadOnlyKeyValueStore();

        ProductDetails productDetails = store.get(productId);

        if(productDetails == null) {
            throw new ResourceNotFoundException("Can't find product for id: "+productId);
        }

        Product product = Product.builder()
                .productId(productDetails.getProductId().toString())
                .category(Category.builder().categoryId(productDetails.getCategoryId().toString()).build())
                .productName(productDetails.getProductName().toString())
                .productPrice(BigDecimal.valueOf(productDetails.getProductPrice()))
                .productDescription(productDetails.getProductDescription().toString())
                .viewCount(productDetails.getViewCount())
                .imagePath(productDetails.getImagePath().toString())
                .build();

        productViewPublisher.publish(product.getProductId(), product.getProductId());
        return product;
    }

}
