package io.osc.bikas.product.data.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.service.ProductDataService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class GrpcProductDataService extends ProductDataServiceGrpc.ProductDataServiceImplBase {

    private final ProductDataService productDataService;

    @Override
    public void getFilteredProducts(CategoryFilterRequest request, StreamObserver<ProductListResponse> responseObserver) {

        var featuredProductList = productDataService.getProductsFilterBy(request.getCategoryId(), request.getFilter());
        var productList = generateProductDetails(featuredProductList);
        var response = buildProductListResponse(productList);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCategoriesOrderedByViewCount(Empty request, StreamObserver<CategoryListResponse> responseObserver) {

        var categoryDtoList = productDataService.findCategoriesOrderedByProductViewCount();
        var categoryDetailsList = generateCategoryDetails(categoryDtoList);

        var response = buildCategoryListResponse(categoryDetailsList);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductById(GetProductByIdRequest request, StreamObserver<ProductDetails> responseObserver) {

        var product = productDataService.findProductById(request.getProductId(), request.getUserId());

        var response = generateProductDetails(product);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllProductById(ProductIdList request, StreamObserver<ProductListResponse> responseObserver) {

        var productIds = request.getProductIdList().stream()
                .map(StringValue::getValue).collect(Collectors.toList());

        var productList = productDataService.findAllProductById(productIds);

        var productDetailsList = generateProductDetails(productList);
        var response = buildProductListResponse(productDetailsList);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    private List<ProductDetails> generateProductDetails(List<Product> products) {
        return products.stream()
                .map(this::generateProductDetails)
                .toList();
    }

    private ProductDetails generateProductDetails(Product product) {
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

    private List<CategoryDetails> generateCategoryDetails(List<CategoryDto> categoryDtoList) {
        return categoryDtoList.stream()
                .map(this::generateCategoryDetails)
                .toList();
    }

    private CategoryDetails generateCategoryDetails(CategoryDto categoryDto) {
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
