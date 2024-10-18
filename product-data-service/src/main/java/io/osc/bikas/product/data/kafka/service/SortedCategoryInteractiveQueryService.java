package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.CategoryDetails;
import com.osc.bikas.avro.CategoryList;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SortedCategoryInteractiveQueryService {

    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;

    public List<CategoryDto> get() {

        ReadOnlyKeyValueStore<String, CategoryList> store = kafkaInteractiveQueryService.getSortedCategoryReadOnlyKeyValueStore();
        CategoryList categoryList = store.get(KafkaConst.SORTED_CATEGORIES_TOPIC_DEFAULT_KEY);

        return generateCategoryDto(categoryList.getCategories());

    }

    private List<CategoryDto> generateCategoryDto(List<CategoryDetails> categoryDetails) {
        return categoryDetails.stream().map(this::generateCategoryDto).collect(Collectors.toList());
    }

    private CategoryDto generateCategoryDto(CategoryDetails categoryDetails) {
        return new CategoryDto(categoryDetails.getCategoryId().toString(),
                categoryDetails.getCategoryName().toString(),
                (long) categoryDetails.getCount());
    }

}
