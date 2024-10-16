package io.osc.bikas.product.data.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.service.ProductDataService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class GrpcProductDataService extends ProductDataServiceGrpc.ProductDataServiceImplBase {

    private final ProductDataService productDataService;

    @Override
    public void getFilteredProducts(CategoryFilterRequest request, StreamObserver<ProductListResponse> responseObserver) {

        String categoryId = request.getCategoryId();
        CategoryFilterRequest.FILTER filter = request.getFilter();

        var featuredProductList = productDataService.getProductsFilterBy(categoryId, filter);
        var productList = convertToProductDetails(featuredProductList);
        var response = buildProductListResponse(productList);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCategoriesOrderedByViewCount(Empty request, StreamObserver<CategoryListResponse> responseObserver) {

        var categoryDtoList = productDataService.findCategoriesOrderedByProductViewCount();

        List<CategoryDetails> categoryDetailsList = convertToCategoryDetails(categoryDtoList);

        var response = buildCategoryListResponse(categoryDetailsList);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductById(StringValue request, StreamObserver<ProductDetails> responseObserver) {
        String productId = request.getValue();
        Product product = productDataService.findProductById(productId);

        var response = convertToProductDetails(product);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllProductById(ProductIdList request, StreamObserver<ProductListResponse> responseObserver) {

        List<String> productIdList = request.getProductIdList().stream().map(StringValue::getValue).toList();

        List<Product> productList = productDataService.findAllProductById(productIdList);

        List<ProductDetails> productDetailsList = convertToProductDetails(productList);
        ProductListResponse response = buildProductListResponse(productDetailsList);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    private List<ProductDetails> convertToProductDetails(List<Product> products) {
        return products.stream()
                .map(this::convertToProductDetails)
                .toList();
    }

    private ProductDetails convertToProductDetails(Product product) {
        return ProductDetails.newBuilder()
                .setProductId(product.getProductId())
                .setCategoryId(product.getCategory().getCategoryId())
                .setProductName(product.getProductName())
                .setProductDescription(product.getProductDescription())
                .setProductPrice(product.getProductPrice().doubleValue())
                .setViewCount(product.getViewCount())
                .build();
    }

    private ProductListResponse buildProductListResponse(List<ProductDetails> productList) {
        return ProductListResponse.newBuilder().addAllProducts(productList).build();
    }

    private List<CategoryDetails> convertToCategoryDetails(List<CategoryDto> categoryDtoList) {
        return categoryDtoList.stream()
                .map(this::convertToCategoryDetails)
                .toList();
    }

    private CategoryDetails convertToCategoryDetails(CategoryDto categoryDto) {
        return CategoryDetails.newBuilder()
                .setCategoryId(categoryDto.categoryId())
                .setCategoryName(categoryDto.categoryName())
                .setViewCount(categoryDto.count().intValue())
                .build();
    }

    private CategoryListResponse buildCategoryListResponse(List<CategoryDetails> categoryDetailsList) {
        return CategoryListResponse.newBuilder().addAllCategories(categoryDetailsList).build();
    }
}
