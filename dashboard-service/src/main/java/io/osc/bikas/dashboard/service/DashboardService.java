package io.osc.bikas.dashboard.service;

import io.osc.bikas.dashboard.dto.*;
import io.osc.bikas.dashboard.exception.InvalidSessionException;
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
    private final CategoriesService categoriesService;
    private final ProductService productService;

    private final CartService cartService;

    public DashboardResponseDto getDashboardData(String userId, String sessionId) {

        if (!sessionDataServiceClient.isSessionValid(userId, sessionId)) {
            throw new InvalidSessionException(userId, sessionId);
        }
        List<DashboardDto> dashboardDtos = new ArrayList<>();

        dashboardDtos.add(getCategoriesDashboard());

        List<String> recentlyViewedProductIdList =
                viewDataServiceClient.getRecentlyViewedProductIdListBy(userId);

        if (recentlyViewedProductIdList.isEmpty()) {
            dashboardDtos.add(getFeaturedProductsDashboard());
        } else {
//            dashboardDtos.addAll(getLastViewedProductsAndFeaturedProducts(recentlyViewedProductIdList));
            dashboardDtos.add(getLastViewedProductsDashboardDto(recentlyViewedProductIdList));
            dashboardDtos.add(getSimilarProductsDashboardDto(recentlyViewedProductIdList));
            dashboardDtos.add(getCartDashboard(userId));
        }
        return new DashboardResponseDto(dashboardDtos);
    }

    private DashboardDto getSimilarProductsDashboardDto(List<String> productIds) {
        return generateSimilarProductDataDto(productService.getSimilarProductsById(productIds));
    }

    private DashboardDto getLastViewedProductsDashboardDto(List<String> productIds) {
        return generateRecentlyViewedProductDataDto(productService.getAllProductById(productIds));
    }

    private DashboardDto getCategoriesDashboard() {
        return generateCategoryDataDto(categoriesService.getAllCategories());
    }

    private DashboardDto getFeaturedProductsDashboard() {
        return generateFeaturedProductDataDto(productService.getFeaturedProducts());
    }

    private DashboardDto generateCategoryDataDto(List<CategoryDto> categoriesDtos) {
        return new CategoriesDto(categoriesDtos);
    }

    //no longer needed
//    private List<DashboardDto> getLastViewedProductsAndFeaturedProducts(List<String> recentlyViewedProductIdList) {
//        List<List<ProductDto>> lastViewedProductAndSimilarProduct =
//                productDataServiceClient.getLastViewedProductDetailsAndSimilarProduct(recentlyViewedProductIdList);
//        return List.of(
//                generateRecentlyViewedProductDataDto(lastViewedProductAndSimilarProduct.get(0)),
//                generateSimilarProductDataDto(lastViewedProductAndSimilarProduct.get(1))
//        );
//    }

    private DashboardDto getCartDashboard(String userId) {
        return generateDashboardCartDataDto(cartService.getCartById(userId));
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
