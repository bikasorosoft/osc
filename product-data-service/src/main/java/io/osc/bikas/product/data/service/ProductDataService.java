package io.osc.bikas.product.data.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.dto.PairDto;
import io.osc.bikas.product.data.dto.ProductDto;
import io.osc.bikas.product.data.kafka.producer.ProductClickPublisher;

import io.osc.bikas.product.data.kafka.service.CategoryDataInteractiveQueryService;
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
    private final CategoryDataInteractiveQueryService categoryDataInteractiveQueryService;
    private final ProductDetailsInteractiveQuery productDetailsInteractiveQuery;
    private final ProductClickPublisher productClickPublisher;

    public List<ProductDto> findFeaturedProducts() {

        List<PairDto> productPairs = popularProductsInteractiveQueryService.getAll();

        return findAllProductByPair(productPairs);

    }

    public List<CategoryDto> findCategoriesOrderedByProductViewCount() {

        List<String> categoryIds = popularProductsInteractiveQueryService.getAllCategoryId();

        return categoryIds.stream().map(
                categoryId -> new CategoryDto(categoryId, categoryDataInteractiveQueryService.get(categoryId))
        ).collect(Collectors.toList());

    }

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
            //change below
            case LOW_TO_HIGH -> findByCategoryIdOrderByProductPriceAsc(categoryId);
            case HIGH_TO_LOW -> findByCategoryIdOrderByProductPriceDesc(categoryId);
            case NEW_FIRST -> findByCategoryIdOrderByProductId(categoryId);
            default -> throw new IllegalArgumentException("Unsupported filter type");
        };

    }

    public List<ProductDto> findSimilarProduct(List<String> lastViewedProductIds) {

        Map<String, LinkedList<PairDto>> mapOfPopularProductByCategory = new HashMap<>();
        lastViewedProductIds.forEach(item -> {
            String categoryId = item.substring(0, 1);
            mapOfPopularProductByCategory.put(categoryId,
                    new LinkedList<>(
                            popularProductsInteractiveQueryService.get(categoryId).stream()
                            .filter(pair -> !lastViewedProductIds.contains(pair.productId()))
                            .collect(Collectors.toList())));
        });

        List<PairDto> similarProductIds = new ArrayList<>();

        lastViewedProductIds.forEach(
                item -> {
                    String categoryId = item.substring(0, 1);
                    LinkedList<PairDto> productIds = mapOfPopularProductByCategory.get(categoryId);
                    similarProductIds.add(productIds.removeFirst());
                }
        );

        if (similarProductIds.size() < 6) {
            String categoryId = lastViewedProductIds.get(0).substring(0, 1);
            LinkedList<PairDto> productIds = mapOfPopularProductByCategory.get(categoryId);
            while (similarProductIds.size() < 6) {
                similarProductIds.add(productIds.removeFirst());
            }
        }

        return findAllProductByPair(similarProductIds);
    }

    private List<ProductDto> findByCategoryIdOrderByProductId(String categoryId) {

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
                Comparator.comparingDouble(ProductDto::getProductPrice);
        Set<ProductDto> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(findPopularProductBy(categoryId));

        return new ArrayList<>(treeSet);

    }

    private List<ProductDto> findPopularProductBy(String categoryId) {
        var productIds = popularProductsInteractiveQueryService.get(categoryId);
        return findAllProductByPair(productIds);
    }

    public ProductDto findProductById(String productId, String userId) {

        var product = findProductById(productId);
        productClickPublisher.publish(userId, productId);

        return product;
    }

    private ProductDto findProductById(String productId) {
        ProductDto productDto = productDetailsInteractiveQuery.get(productId);
        productDto.setProductId(productId);
        return productDto;
    }

    public List<ProductDto> findAllProductById(List<String> productIdList) {

        return productIdList.stream()
                .map(this::findProductById)
                .collect(Collectors.toList());

    }

    public List<ProductDto> findAllProductByPair(List<PairDto> pairDtoList) {

        return pairDtoList.stream()
                .map(item -> {
                    ProductDto productDto = findProductById(item.productId());
                    productDto.setViewCount(item.viewCount());
                    return productDto;
                })
                .collect(Collectors.toList());

    }
}
