package io.osc.bikas.product.data.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductDataService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

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
        return productRepository.findById(productId).get();
    }
}
