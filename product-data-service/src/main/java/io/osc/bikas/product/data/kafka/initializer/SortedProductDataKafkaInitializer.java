package io.osc.bikas.product.data.kafka.initializer;

import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.kafka.producer.SortedCategoryDataPublisher;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

//@Configuration
@RequiredArgsConstructor
public class SortedProductDataKafkaInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final SortedCategoryDataPublisher sortedCategoryDataPublisher;

    @Override
    public void run(String... args) throws Exception {
        List<CategoryDto> categories = productRepository.findCategoriesOrderedByProductViewCount();
        sortedCategoryDataPublisher.publish(categories);
    }
}
