package io.osc.bikas.dashboard.service;

import com.osc.bikas.proto.CategoryFilterRequest;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final GrpcProductDataServiceClient productDataServiceClient;
    private final GrpcSessionDataServiceClient sessionDataServiceClient;
    private final GrpcViewDataServiceClient viewDataServiceClient;

    public DataObjectDto getDashboardData(String userId, String sessionId) {

        if (!sessionDataServiceClient.isSessionValid(userId, sessionId)) {
            throw new InvalidSessionException(userId, sessionId);
        }

        List<DashboardDto> dashboardDtos = new ArrayList<>();

        List<CategoryDto> categories = productDataServiceClient.fetchCategoriesOrderByViewCount();

        dashboardDtos.add(generateCategoryDataDto(categories));

        //fetch user last viewed product list
        List<String> recentlyViewedProductIdListBy =
                viewDataServiceClient.getRecentlyViewedProductIdListBy(userId);

        //check if list is empty (empty list mean user does not have view history)
        if (recentlyViewedProductIdListBy.isEmpty()) {
            //get featured products
            List<ProductDto> featuredProducts =
                    productDataServiceClient.getFeaturedProducts();
            //add it to response data dto
            dashboardDtos.add(generateFeaturedProductDataDto(featuredProducts));
        } else {
            //user have view history
            //get last viewed product id list
            List<ProductDto> lastViewedProducts =
                    productDataServiceClient.getAllProductById(recentlyViewedProductIdListBy);
            //get last viewed product details by product id
            dashboardDtos.add(generateRecentlyViewedProductDataDto(lastViewedProducts));
            //get featured product details

            //get cart details

        }

        //fetch

        return new DataObjectDto(dashboardDtos);
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


}
