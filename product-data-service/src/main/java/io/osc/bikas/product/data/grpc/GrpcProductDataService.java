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

        var featuredProductList = productDataService.filterProduct(categoryId, filter);
        var productList = new ArrayList<ProductDetails>();

        for (Product product : featuredProductList) {
            ProductDetails productDetails = ProductDetails.newBuilder()
                    .setProductId(product.getProductId())
                    .setCategoryId(product.getCategory().getCategoryId())
                    .setProductName(product.getProductName())
                    .setProductDescription(product.getProductDescription())
                    .setProductPrice(product.getProductPrice().doubleValue())
                    .setCount(product.getCount())
                    .build();
            productList.add(productDetails);
        }

        ProductListResponse response = ProductListResponse.newBuilder().addAllProducts(productList).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCategoriesOrderedByViewCount(Empty request, StreamObserver<CategoryListResponse> responseObserver) {

        var categoryList = productDataService.findCategoriesOrderedByProductViewCount();

        List<Category> CategoryList = new ArrayList<Category>();

        for (CategoryDto categoryDto : categoryList) {
            Category category = Category.newBuilder()
                    .setCategoryId(categoryDto.categoryId())
                    .setCategoryName(categoryDto.categoryName())
                    .setCount(categoryDto.count().intValue())
                    .build();
            CategoryList.add(category);
        }

        var response = CategoryListResponse.newBuilder().addAllCategories(CategoryList).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductDetails(StringValue request, StreamObserver<ProductDetails> responseObserver) {
        String productId = request.getValue();
        Product product = productDataService.findProductById(productId);

        ProductDetails response = ProductDetails.newBuilder()
                .setProductId(product.getProductId())
                .setCategoryId(product.getCategory().getCategoryId())
                .setProductName(product.getProductName())
                .setProductDescription(product.getProductDescription())
                .setProductPrice(product.getProductPrice().doubleValue())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
