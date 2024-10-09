package io.osc.bikas.dashboard.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.dashboard.dto.*;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final GrpcProductDataServiceClient productDataServiceClient;

    public DataObjectDto getDashboardData(String userId) {

        //let user don't have product view history

        //get categories
        List<CategoryDto> categories = productDataServiceClient.fetchCategoriesOrderByViewCount();

        //get featured products
        List<ProductDto> featuredProducts = productDataServiceClient.fetchFeaturedProducts();

        DataDto categoriesDataDto = DataDto.builder()
                .type("Categories")
                .categories(categories)
                .build();

        DataDto featuredProductsDataDto = DataDto.builder()
                .type("Featured Products")
                .featuredProducts(featuredProducts)
                .build();

        return new DataObjectDto(Arrays.asList(categoriesDataDto, featuredProductsDataDto));

    }

    public ProductDetailsAndSimilarProductListDto getProductDetailsAndSimilarProduct(String productId, String categoryId) {

        ProductDto productDto = productDataServiceClient.fetchProductDetails(productId);

        List<ProductDto> productDtoList = productDataServiceClient.fetchFilteredProducts(categoryId, CategoryFilterRequest.FILTER.POPULAR);

        productDtoList.remove(productDto);

        return ProductDetailsAndSimilarProductListDto.builder()
                        .productId(productDto.productId())
                        .categoryId(productDto.categoryId())
                        .productName(productDto.productName())
                        .productDescription(productDto.productDescription())
                        .productPrice(productDto.productPrice())
                        .similarProducts(productDtoList)
                        .build();
    }

    public FilterProductResponse getFilteredProduct(String categoryId, String filterString) {

        var productList = switch (filterString) {
            case "P" -> productDataServiceClient.fetchFilteredProducts(categoryId, CategoryFilterRequest.FILTER.POPULAR);
            case "LH" -> productDataServiceClient.fetchFilteredProducts(categoryId, CategoryFilterRequest.FILTER.LOW_TO_HIGH);
            case "HL" -> productDataServiceClient.fetchFilteredProducts(categoryId, CategoryFilterRequest.FILTER.HIGH_TO_LOW);
            case "NF" -> productDataServiceClient.fetchFilteredProducts(categoryId, CategoryFilterRequest.FILTER.NEW_FIRST);
            default -> throw new IllegalStateException("Unexpected value: " + filterString);
        };
        return new FilterProductResponse(productList);
    }
}
