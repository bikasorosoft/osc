package io.osc.bikas.product.data.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.kafka.producer.ProductClickPublisher;
import io.osc.bikas.product.data.kafka.producer.ProductDetailsPublisher;
import io.osc.bikas.product.data.kafka.producer.ProductViewPublisher;
import io.osc.bikas.product.data.kafka.service.KafkaInteractiveQueryService;
import io.osc.bikas.product.data.kafka.service.PopularProductsInteractiveQueryService;
import io.osc.bikas.product.data.kafka.service.ProductDetailsInteractiveQuery;
import io.osc.bikas.product.data.kafka.service.SortedCategoryInteractiveQueryService;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.osc.bikas.proto.CategoryFilterRequest.FILTER.*;

@Service
@RequiredArgsConstructor
public class ProductDataService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductViewPublisher productViewPublisher;
    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;
    private final ProductDetailsPublisher productDetailsPublisher;
    private final SortedCategoryInteractiveQueryService sortedCategoryInteractiveQueryService;
    private final PopularProductsInteractiveQueryService popularProductsInteractiveQueryService;
    private final ProductDetailsInteractiveQuery productDetailsInteractiveQuery;
    private final ProductClickPublisher productClickPublisher;

    public List<Product> findFeaturedProducts() {

        List<String> productIds = popularProductsInteractiveQueryService.getAll();

        return findAllProductById(productIds);

    }

    public List<CategoryDto> findCategoriesOrderedByProductViewCount() {

        return sortedCategoryInteractiveQueryService.get();

    }


    public List<Product> getProductsFilterBy(String categoryId, CategoryFilterRequest.FILTER filter) {

        if (Objects.isNull(filter)) {
            throw new IllegalArgumentException("Filter cannot be null");
        }

        boolean isCategoryIdNullOrEmpty = Objects.isNull(categoryId) || categoryId.isEmpty();

        if (isCategoryIdNullOrEmpty && filter == POPULAR) {
            return findFeaturedProducts();
        }

        return switch (filter) {
            case POPULAR -> findPopularProductBy(categoryId);
            //change below
            case LOW_TO_HIGH -> findByCategoryIdOrderByProductPriceAsc(categoryId);
            case HIGH_TO_LOW -> findByCategoryIdOrderByProductPriceDesc(categoryId);
            case NEW_FIRST -> findByCategoryIdOrderByProductId(categoryId);
            default -> throw new IllegalArgumentException("Unsupported filter type");
        };

    }

    public List<Product> findSimilarProduct(List<String> lastViewedProductIds) {

        Map<String, List<String>> mapOfPopularProductByCategory = new HashMap<>();
        lastViewedProductIds.forEach(item -> {
            String categoryId = item.substring(0, 1);
            mapOfPopularProductByCategory.put(categoryId, new LinkedList<>(popularProductsInteractiveQueryService.get(categoryId)));
        });

        List<String> similarProductIds = new ArrayList<>();

        lastViewedProductIds.forEach(
                item -> {
                    String categoryId = item.substring(0, 1);
                    List<String> productIds = mapOfPopularProductByCategory.get(categoryId);
                    similarProductIds.add(productIds.removeFirst());
                }
        );

        if (similarProductIds.size() < 6) {
            String categoryId = lastViewedProductIds.get(0).substring(0, 1);
            List<String> productIds = mapOfPopularProductByCategory.get(categoryId);
            while (similarProductIds.size() < 6) {
                similarProductIds.add(productIds.removeFirst());
            }
        }

        return findAllProductById(similarProductIds);

    }

    private List<Product> findByCategoryIdOrderByProductId(String categoryId) {

        Comparator<Product> comparator =
                Comparator.comparing(Product::getProductId).reversed();
        Set<Product> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(findPopularProductBy(categoryId));

        return new ArrayList<>(treeSet);

    }

    private List<Product> findByCategoryIdOrderByProductPriceDesc(String categoryId) {
        List<Product> products = findByCategoryIdOrderByProductPriceAsc(categoryId);
        Collections.reverse(products);
        return products;
    }

    private List<Product> findByCategoryIdOrderByProductPriceAsc(String categoryId) {

        Comparator<Product> comparator =
                Comparator.comparingDouble( (Product item) -> item.getProductPrice().doubleValue())
                .reversed();
        Set<Product> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(findPopularProductBy(categoryId));

        return new ArrayList<>(treeSet);

    }

    private List<Product> findPopularProductBy(String categoryId) {
        var productIds = popularProductsInteractiveQueryService.get(categoryId);
        return findAllProductById(productIds);
    }

    public Product findProductById(String productId, String userId) {

        var product = findProductById(productId);
        productClickPublisher.publish(userId, productId);

        return product;
    }

    private Product findProductById(String productId) {
        return productDetailsInteractiveQuery.get(productId);
    }

    public List<Product> findAllProductById(List<String> productIdList) {

        return productIdList.stream()
                .map(this::findProductById)
                .collect(Collectors.toList());

    }
}
