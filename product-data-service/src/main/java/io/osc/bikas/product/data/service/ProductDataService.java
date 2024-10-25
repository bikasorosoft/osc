package io.osc.bikas.product.data.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.product.data.dto.ProductDto;
import io.osc.bikas.product.data.kafka.producer.ProductClickPublisher;

import io.osc.bikas.product.data.kafka.service.PopularProductsInteractiveQueryService;
import io.osc.bikas.product.data.kafka.service.ProductDetailsInteractiveQuery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.osc.bikas.proto.CategoryFilterRequest.FILTER.*;

@Service
@RequiredArgsConstructor
public class ProductDataService {

    private final PopularProductsInteractiveQueryService popularProductsInteractiveQueryService;
    private final ProductDetailsInteractiveQuery productDetailsInteractiveQuery;
    private final ProductClickPublisher productClickPublisher;

    public List<ProductDto> getProductsFilterBy(String categoryId, CategoryFilterRequest.FILTER filter) {

        if (Objects.isNull(filter)) {
            throw new IllegalArgumentException("Filter cannot be null");
        }

        boolean isCategoryIdNullOrEmpty = Objects.isNull(categoryId) || categoryId.isEmpty();

        if (isCategoryIdNullOrEmpty && filter == POPULAR) {
            return findFeaturedProducts();
        }

        return switch (filter) {
            case POPULAR -> findPopularProductBy(categoryId);
            case LOW_TO_HIGH -> findByCategoryIdOrderByProductPriceAsc(categoryId);
            case HIGH_TO_LOW -> findByCategoryIdOrderByProductPriceDesc(categoryId);
            case NEW_FIRST -> findLatestProductsBy(categoryId);
            default -> throw new IllegalArgumentException("Unsupported filter type");
        };
    }

    private List<ProductDto> findFeaturedProducts() {
        return popularProductsInteractiveQueryService.getAllProducts();
    }

    public List<ProductDto> findSimilarProduct(List<String> lastViewedProductIds) {

        Map<String, LinkedList<ProductDto>> categoryIdToPopularProductMap =
                getCategoryIdToPopularProductMapBy(lastViewedProductIds);

        List<ProductDto> similarProducts = new ArrayList<>();

        lastViewedProductIds.forEach(
                productId -> {
                    String categoryId = productId.substring(0, 1);
                    LinkedList<ProductDto> productIds = categoryIdToPopularProductMap.get(categoryId);
                    similarProducts.add(productIds.removeFirst());
                }
        );

        if (similarProducts.size() < 6) {
            String categoryId = lastViewedProductIds.get(0).substring(0, 1);
            LinkedList<ProductDto> products = categoryIdToPopularProductMap.get(categoryId);
            while (similarProducts.size() < 6) {
                similarProducts.add(products.removeFirst());
            }
        }
        return similarProducts;
    }

    private Map<String, LinkedList<ProductDto>> getCategoryIdToPopularProductMapBy(List<String> lastViewedProductIds) {
        HashMap<String, LinkedList<ProductDto>> categoryIdToPopularProductMap = new HashMap<>();
        lastViewedProductIds.forEach(item -> {
            String categoryId = item.substring(0, 1);
            categoryIdToPopularProductMap.put(categoryId,
                            popularProductsInteractiveQueryService.getPopularProductsBy(categoryId).stream()
                                    .filter(product -> !lastViewedProductIds.contains(product.getProductId()))
                                    .collect(Collectors.toCollection(LinkedList::new)));
        });
        return categoryIdToPopularProductMap;
    }

    private List<ProductDto> findLatestProductsBy(String categoryId) {

        Comparator<ProductDto> comparator =
                Comparator.comparing(ProductDto::getProductId).reversed();

        Set<ProductDto> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(findPopularProductBy(categoryId));

        return new ArrayList<>(treeSet);
    }

    private List<ProductDto> findByCategoryIdOrderByProductPriceDesc(String categoryId) {
        List<ProductDto> products = findByCategoryIdOrderByProductPriceAsc(categoryId);
        Collections.reverse(products);
        return products;
    }

    private List<ProductDto> findByCategoryIdOrderByProductPriceAsc(String categoryId) {

        Comparator<ProductDto> comparator =
                Comparator.comparingDouble(ProductDto::getProductPrice)
                        .thenComparing(ProductDto::getProductId);

        Set<ProductDto> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(findPopularProductBy(categoryId));

        return new ArrayList<>(treeSet);

    }

    private List<ProductDto> findPopularProductBy(String categoryId) {
        return popularProductsInteractiveQueryService.getPopularProductsBy(categoryId);
    }

    public ProductDto findProductById(String productId, String userId) {

        var product = findProductById(productId);
        productClickPublisher.publish(userId, productId);

        return product;
    }

    private ProductDto findProductById(String productId) {
        ProductDto product = productDetailsInteractiveQuery.get(productId);
        product.setProductId(productId);
        return product;
    }

    public List<ProductDto> findAllProductById(List<String> productIds) {
        return productIds.stream()
                .map(this::findProductById)
                .collect(Collectors.toList());
    }

}