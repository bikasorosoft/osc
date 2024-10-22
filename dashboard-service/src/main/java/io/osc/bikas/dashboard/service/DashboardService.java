package io.osc.bikas.dashboard.service;

import io.osc.bikas.dashboard.dto.*;
import io.osc.bikas.dashboard.exception.InvalidSessionException;
import io.osc.bikas.dashboard.grpc.GrpcCartDataServiceClient;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import io.osc.bikas.dashboard.grpc.GrpcSessionDataServiceClient;
import io.osc.bikas.dashboard.grpc.GrpcViewDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final GrpcProductDataServiceClient productDataServiceClient;
    private final GrpcSessionDataServiceClient sessionDataServiceClient;
    private final GrpcViewDataServiceClient viewDataServiceClient;
    private final GrpcCartDataServiceClient cartDataServiceClient;

    private final CartService cartService;

    public DashboardResponseDto getDashboardData(String userId, String sessionId) {

        if (!sessionDataServiceClient.isSessionValid(userId, sessionId)) {
            throw new InvalidSessionException(userId, sessionId);
        }

        List<DashboardDto> dashboardDtos = new ArrayList<>();

        List<CategoryDto> categories = productDataServiceClient.fetchCategoriesOrderByViewCount();

        dashboardDtos.add(generateCategoryDataDto(categories));

        //fetch user last viewed product list
        List<String> recentlyViewedProductIdList =
                viewDataServiceClient.getRecentlyViewedProductIdListBy(userId);

        //check if list is empty (empty list mean user does not have view history)
        if (recentlyViewedProductIdList.isEmpty()) {
            //get featured products
            List<ProductDto> featuredProducts =
                    productDataServiceClient.getFeaturedProducts();
            //add it to response data dto
            dashboardDtos.add(generateFeaturedProductDataDto(featuredProducts));
        } else {
            //user have view history
            //get last viewed product id list
            List<List<ProductDto>> lastViewedProductAndSimilarProduct = productDataServiceClient.getSimilarProductsById(recentlyViewedProductIdList);
            //get last viewed product details by product id
            dashboardDtos.add(generateRecentlyViewedProductDataDto(lastViewedProductAndSimilarProduct.get(0)));
            //get similar product details
            dashboardDtos.add(generateSimilarProductDataDto(lastViewedProductAndSimilarProduct.get(1)));
            //get cart details
            CartDto cart = cartService.getCart(userId);
            dashboardDtos.add(generateDashboardCartDataDto(cart));

        }

        //fetch

        return new DashboardResponseDto(dashboardDtos);
    }



    private DashboardDto generateCategoryDataDto(List<CategoryDto> categoriesDtos) {
        return new CategoriesDto(categoriesDtos);
    }

    private DashboardDto generateFeaturedProductDataDto(List<ProductDto> featuredProducts) {
        return new FeaturedProductsDto(featuredProducts);
    }

    private DashboardDto generateRecentlyViewedProductDataDto(List<ProductDto> lastViewedProducts) {
        return new RecentlyViewedProducts(lastViewedProducts);
    }

    private DashboardDto generateSimilarProductDataDto(List<ProductDto> similarProducts) {
        return new SimilarProducts(similarProducts);
    }

    private DashboardDto generateDashboardCartDataDto(CartDto cartDto) {
        return new DashboardCartDto(cartDto);
    }




}
