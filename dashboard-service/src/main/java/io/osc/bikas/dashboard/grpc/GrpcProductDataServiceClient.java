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

    public ProductDto getProductById(String userId, String productId) {
        ProductDetails productDetails = productDataServiceBlockingStub.getProductById(GetProductByIdRequest.newBuilder()
                .setUserId(userId)
                .setProductId(productId).build());
        return generateProductDto(productDetails);
    }

    public List<List<ProductDto>> getLastViewedProductDetailsAndSimilarProduct(List<String> productIds) {

        List<StringValue> stringValues = generateStringValue(productIds);

        GetSimilarProductResponse similarProducts = productDataServiceBlockingStub.getSimilarProducts(ProductIdList.newBuilder().addAllProductId(stringValues).build());

        List<List<ProductDto>> recentlyViewedProductAndSimilarProduct = new ArrayList<>();

        recentlyViewedProductAndSimilarProduct.add(generateProductDto(similarProducts.getLastVisitedProduct().getProductsList()));
        recentlyViewedProductAndSimilarProduct.add(generateProductDto(similarProducts.getSimilarProducts().getProductsList()));

        return recentlyViewedProductAndSimilarProduct;

    }

    public List<ProductDto> getAllProductById(List<String> productIds) {

        var stringValues = generateStringValue(productIds);

        ProductIdList request = ProductIdList.newBuilder().addAllProductId(stringValues).build();
        ProductListResponse response = productDataServiceBlockingStub.getAllProductById(request);

        return generateProductDto(response.getProductsList());
    }

    public List<ProductDto> getSimilarProductsById(List<String> productIds) {

        var stringValues = generateStringValue(productIds);

        ProductIdList request = ProductIdList.newBuilder().addAllProductId(stringValues).build();
        ProductListResponse response = productDataServiceBlockingStub.getSimilarProductsById(request);

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

    public List<CategoryDto> getCategories() {

        CategoryListResponse response =
                productDataServiceBlockingStub.getCategoriesOrderedByViewCount(Empty.newBuilder().build());
        return generateCategoryDto(response.getCategoriesList());
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
