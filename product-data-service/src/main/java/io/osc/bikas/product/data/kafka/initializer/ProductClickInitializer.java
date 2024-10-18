package io.osc.bikas.product.data.kafka.initializer;

import io.osc.bikas.product.data.kafka.producer.ProductClickPublisher;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;

//@Configuration
@RequiredArgsConstructor
public class ProductClickInitializer implements CommandLineRunner {

    private final ProductClickPublisher productClickPublisher;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
//should only run once to populate view count form db to kafka topic if there is no data in topic
//migrate product view count from db to topic
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            for (int i = 0; i < p.getViewCount(); i++) {
                productClickPublisher.publish("test-user", p.getProductId());
            }
        }
    }
}
