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

@Service
public class GrpcProductDataServiceClient {

    @GrpcClient("product-data-service")
    private ProductDataServiceGrpc.ProductDataServiceBlockingStub productDataServiceBlockingStub;

    public ProductDto fetchProductDetails(String productId) {
        ProductDetails productDetails = productDataServiceBlockingStub.getProductDetails(StringValue.newBuilder().setValue(productId).build());
        return toProductDto(productDetails);
    }

    public List<ProductDto> fetchFilteredProducts(String categoryId, CategoryFilterRequest.FILTER filter) {
        CategoryFilterRequest categoryFilterRequest = CategoryFilterRequest.newBuilder().setCategoryId(categoryId).setFilter(filter).build();
        ProductListResponse filteredProducts = productDataServiceBlockingStub.getFilteredProducts(categoryFilterRequest);
        return toProductDto(filteredProducts);
    }

    public List<CategoryDto> fetchCategoriesOrderByViewCount() {

        CategoryListResponse categories =
                productDataServiceBlockingStub.getCategoriesOrderedByViewCount(Empty.newBuilder().build());
        return toCategoryDto(categories);
    }

    public List<ProductDto> fetchFeaturedProducts() {
        ProductListResponse filteredProducts = productDataServiceBlockingStub.getFilteredProducts(CategoryFilterRequest.newBuilder().build());
        return toProductDto(filteredProducts);
    }

    private List<CategoryDto> toCategoryDto(CategoryListResponse categoryListResponse) {

        List<CategoryDto> categoryDtoList = new ArrayList<>();

        for (Category category : categoryListResponse.getCategoriesList()) {
            CategoryDto categoryDto = CategoryDto.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .count(category.getViewCount())
                    .build();
            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }

    private List<ProductDto> toProductDto(ProductListResponse productListResponse) {

        List<ProductDto> productDtoList = new ArrayList<>();

        for (ProductDetails product : productListResponse.getProductsList()) {

            ProductDto productDto = ProductDto.builder()
                    .productId(product.getProductId())
                    .categoryId(product.getCategoryId())
                    .productName(product.getProductName())
                    .productPrice(product.getProductPrice())
                    .productDescription(product.getProductDescription())
                    .viewCount(product.getViewCount())
                    .build();
            productDtoList.add(productDto);
        }

        return productDtoList;

    }

    private ProductDto toProductDto(ProductDetails proto) {

        ProductDto dto = ProductDto.builder().build();

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
