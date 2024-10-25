package io.osc.bikas.product.data.service;

import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.kafka.service.CategoryDataInteractiveQueryService;
import io.osc.bikas.product.data.kafka.service.PopularProductsInteractiveQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryDataService {

    private final PopularProductsInteractiveQueryService popularProductsInteractiveQueryService;
    private final CategoryDataInteractiveQueryService categoryDataInteractiveQueryService;

    public List<CategoryDto> findCategoriesOrderedByProductViewCount() {

        List<String> categoryIds = popularProductsInteractiveQueryService.getAllCategoryId();

        return categoryIds.stream().map(
                categoryId -> new CategoryDto(categoryId, categoryDataInteractiveQueryService.get(categoryId))
        ).collect(Collectors.toList());

    }

}
