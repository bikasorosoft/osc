package io.osc.bikas.dashboard.service;

import io.osc.bikas.dashboard.dto.CategoryDto;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final GrpcProductDataServiceClient productDataServiceClient;

    public List<CategoryDto> getAllCategories() {
        return productDataServiceClient.getCategories();
    }

}
