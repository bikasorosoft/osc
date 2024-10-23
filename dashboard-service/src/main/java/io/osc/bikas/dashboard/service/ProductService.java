package io.osc.bikas.dashboard.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.dashboard.dto.DashboardDto;
import io.osc.bikas.dashboard.dto.FilterProductResponse;
import io.osc.bikas.dashboard.dto.ProductDetailsAndSimilarProductResponse;
import io.osc.bikas.dashboard.dto.ProductDto;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.osc.bikas.proto.CategoryFilterRequest.FILTER.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final GrpcProductDataServiceClient productDataServiceClient;

    public ProductDto getProductById(String productId, String userId) {
        return productDataServiceClient.getProductById(userId, productId);
    }

    public List<ProductDto> getAllProductById(List<String> productIds) {
        return productDataServiceClient.getAllProductById(productIds);
    }

    public ProductDetailsAndSimilarProductResponse getProductDetailsAndSimilarProduct(String productId, String userId, String categoryId) {

        ProductDto productDto = getProductById(productId, userId);

        List<ProductDto> similarProducts = getProductsFilterBy(categoryId, POPULAR);
        similarProducts.remove(productDto);

        return generateProductRetailsAndSimilarProductResponse(productDto, similarProducts);
    }

    public FilterProductResponse getProductsFilterBy(String categoryId, String filterString) {

        var productList = switch (filterString) {
            case "P" -> getProductsFilterBy(categoryId, POPULAR);
            case "LH" -> getProductsFilterBy(categoryId, LOW_TO_HIGH);
            case "HL" -> getProductsFilterBy(categoryId, HIGH_TO_LOW);
            case "NF" -> getProductsFilterBy(categoryId, NEW_FIRST);
            default -> throw new IllegalStateException("Unexpected value: " + filterString);
        };
        return new FilterProductResponse(productList);
    }

    private List<ProductDto> getProductsFilterBy(String categoryId, CategoryFilterRequest.FILTER filter) {
        return productDataServiceClient.getProductsFilterBy(categoryId, filter);
    }

    public List<ProductDto> getFeaturedProducts() {
        return getProductsFilterBy("", POPULAR);
    }

    private ProductDetailsAndSimilarProductResponse generateProductRetailsAndSimilarProductResponse(ProductDto productDto, List<ProductDto> similarProducts) {
        return ProductDetailsAndSimilarProductResponse.builder()
                .productId(productDto.productId())
                .categoryId(productDto.categoryId())
                .productName(productDto.productName())
                .productDescription(productDto.productDescription())
                .productPrice(productDto.productPrice())
                .similarProducts(similarProducts)
                .build();
    }

    public List<ProductDto> getSimilarProductsById(List<String> productIds) {
        return productDataServiceClient.getSimilarProductsById(productIds);
    }
}
