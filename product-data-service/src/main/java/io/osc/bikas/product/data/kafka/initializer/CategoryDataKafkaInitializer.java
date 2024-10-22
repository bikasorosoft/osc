package io.osc.bikas.product.data.kafka.initializer;

import io.osc.bikas.product.data.kafka.producer.CategoryDetailsPublisher;
import io.osc.bikas.product.data.kafka.producer.ProductClickPublisher;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Order(3)
@RequiredArgsConstructor
public class CategoryDataKafkaInitializer implements CommandLineRunner {

    @Value("${app.kafka.initialize.category-data}")
    String initialize;

    private final CategoryRepository categoryRepository;
    private final CategoryDetailsPublisher categoryDetailsPublisher;

    @Override
    public void run(String... args) throws Exception {

        if (!Boolean.parseBoolean(initialize)) {
            return;
        }

        List<Category> categories = categoryRepository.findAll();

        for (Category c : categories) {
            categoryDetailsPublisher.publish(c.getCategoryId(), c.getCategoryName());
            Thread.sleep(200);
        }

    }
}
