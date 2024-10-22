package io.osc.bikas.dashboard.service;

import com.osc.bikas.proto.CategoryFilterRequest;
import io.osc.bikas.dashboard.dto.FilterProductResponse;
import io.osc.bikas.dashboard.dto.ProductDetailsAndSimilarProductListDto;
import io.osc.bikas.dashboard.dto.ProductDto;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final GrpcProductDataServiceClient productDataServiceClient;

    public ProductDetailsAndSimilarProductListDto getProductDetailsAndSimilarProduct(String productId,String userId, String categoryId) {

        ProductDto productDto = productDataServiceClient.getProductDetailsById(userId, productId);

        List<ProductDto> productDtoList = productDataServiceClient.getProductsFilterBy(categoryId, CategoryFilterRequest.FILTER.POPULAR);

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

    public FilterProductResponse getProductsFilterBy(String categoryId, String filterString) {

        var productList = switch (filterString) {
            case "P" -> productDataServiceClient.getProductsFilterBy(categoryId, CategoryFilterRequest.FILTER.POPULAR);
            case "LH" ->
                    productDataServiceClient.getProductsFilterBy(categoryId, CategoryFilterRequest.FILTER.LOW_TO_HIGH);
            case "HL" ->
                    productDataServiceClient.getProductsFilterBy(categoryId, CategoryFilterRequest.FILTER.HIGH_TO_LOW);
            case "NF" ->
                    productDataServiceClient.getProductsFilterBy(categoryId, CategoryFilterRequest.FILTER.NEW_FIRST);
            default -> throw new IllegalStateException("Unexpected value: " + filterString);
        };
        return new FilterProductResponse(productList);
    }

}
