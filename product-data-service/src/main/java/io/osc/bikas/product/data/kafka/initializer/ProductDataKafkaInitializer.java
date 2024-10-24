package io.osc.bikas.product.data.kafka.initializer;

import io.osc.bikas.product.data.kafka.producer.ProductDetailsPublisher;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class ProductDataKafkaInitializer implements CommandLineRunner {

    @Value("${app.kafka.initialize.product-data}")
    String initialize;

    private final ProductDetailsPublisher publisher;
    private final ProductRepository repository;

    @Override
    public void run(String... args) throws Exception {

        if (!Boolean.parseBoolean(initialize)) {
            return;
        }

        //Run only once to populate data to topic
        List<Product> products = repository.findAll();

        for (Product product : products) {
            publisher.publish(product.getProductId(), product);
            Thread.sleep(200);
        }

    }
}

