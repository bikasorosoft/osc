package io.osc.bikas.dashboard.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import io.osc.bikas.dashboard.dto.CategoryDto;
import io.osc.bikas.dashboard.dto.ProductDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrpcProductDataServiceClient {

    @GrpcClient("product-data-service")
    private ProductDataServiceGrpc.ProductDataServiceBlockingStub productDataServiceBlockingStub;

    public ProductDto getProductDetailsById(String productId) {
        ProductDetails productDetails = productDataServiceBlockingStub.getProductById(StringValue.newBuilder().setValue(productId).build());
        return generateProductDto(productDetails);
    }

    public List<ProductDto> getAllProductById(List<String> productIdList) {

        var stringValues = generateStringValue(productIdList);
        ProductListResponse response = productDataServiceBlockingStub.getAllProductById(ProductIdList.newBuilder().addAllProductId(stringValues).build());

        return generateProductDto(response.getProductsList());

    }

    private List<StringValue> generateStringValue(List<String> stringList) {
        return stringList.stream().map(StringValue::of)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsFilterBy(String categoryId, CategoryFilterRequest.FILTER filter) {
        CategoryFilterRequest categoryFilterRequest = CategoryFilterRequest.newBuilder().setCategoryId(categoryId).setFilter(filter).build();
        ProductListResponse filteredProducts = productDataServiceBlockingStub.getFilteredProducts(categoryFilterRequest);
        return generateProductDto(filteredProducts.getProductsList());
    }

    public List<CategoryDto> fetchCategoriesOrderByViewCount() {

        CategoryListResponse categories =
                productDataServiceBlockingStub.getCategoriesOrderedByViewCount(Empty.newBuilder().build());
        return generateCategoryDto(categories.getCategoriesList());
    }

    public List<ProductDto> getFeaturedProducts() {
        ProductListResponse filteredProducts = productDataServiceBlockingStub.getFilteredProducts(CategoryFilterRequest.newBuilder().build());
        return generateProductDto(filteredProducts.getProductsList());
    }



    private List<CategoryDto> generateCategoryDto(List<CategoryDetails> categoryListResponse) {
        return categoryListResponse.stream()
                .map(this::generateCategoryDto)
                .collect(Collectors.toList());
    }

    private CategoryDto generateCategoryDto(CategoryDetails categoryDetails) {
        return CategoryDto.builder()
                .categoryId(categoryDetails.getCategoryId())
                .categoryName(categoryDetails.getCategoryName())
                .count(categoryDetails.getViewCount())
                .build();
    }

    private List<ProductDto> generateProductDto(List<ProductDetails> productDetailsList) {

        return productDetailsList.stream()
                .map(this::generateProductDto)
                .collect(Collectors.toList());
    }

    private ProductDto generateProductDto(ProductDetails proto) {
        return ProductDto.builder()
                .productId(proto.getProductId())
                .categoryId(proto.getCategoryId())
                .productName(proto.getProductName())
                .productPrice(proto.getProductPrice())
                .productDescription(proto.getProductDescription())
                .viewCount(proto.getViewCount())
                .build();
    }


}
