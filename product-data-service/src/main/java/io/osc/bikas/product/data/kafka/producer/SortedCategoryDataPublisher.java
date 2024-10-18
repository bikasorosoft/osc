package io.osc.bikas.product.data.kafka.producer;

import com.osc.bikas.avro.CategoryDetails;
import com.osc.bikas.avro.CategoryList;
import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SortedCategoryDataPublisher {

    private final KafkaTemplate<String, CategoryList> kafkaTemplate;

    public void publish(List<CategoryDto> categoryDtoList) {

        var value = CategoryList.newBuilder()
                .setCategories(generateCategoryDetails(categoryDtoList)).build();

        kafkaTemplate.send(
                KafkaConst.SORTED_CATEGORIES_TOPIC,
                KafkaConst.SORTED_CATEGORIES_TOPIC_DEFAULT_KEY,
                value);

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
                .setCount(categoryDto.count().intValue())
                .build();
    }

}
