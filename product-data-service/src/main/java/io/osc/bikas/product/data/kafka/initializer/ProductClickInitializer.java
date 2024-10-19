package io.osc.bikas.product.data.kafka.initializer;

import io.osc.bikas.product.data.kafka.producer.ProductClickPublisher;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Random;

//@Component
@Order(2)
@RequiredArgsConstructor
public class ProductClickInitializer implements CommandLineRunner {

    private final ProductClickPublisher productClickPublisher;
    private final ProductRepository productRepository;

    //should only run once to populate view count form db to kafka topic if there is no data in topic
    //migrate product view count from db to topic
    @Override
    public void run(String... args) throws Exception {

        Thread.sleep(Duration.ofMinutes(1).toMillis());
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            p.setViewCount(0);
        }
        productRepository.saveAll(products);
        Random random = new Random();

        for (Product p: products) {
            int i = random.nextInt(15, 50);
            while (i > 0) {
                productClickPublisher.publish("test-user", p.getProductId());
                i--;
                Thread.sleep(10);
            }
            Thread.sleep(100);
        }

    }
}
