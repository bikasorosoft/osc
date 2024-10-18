package io.osc.bikas.product.data.kafka.initializer;

import io.osc.bikas.product.data.kafka.producer.ProductDetailsPublisher;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//@Configuration
@RequiredArgsConstructor
public class ProductDataKafkaInitializer implements CommandLineRunner {

    private final ProductDetailsPublisher publisher;
    private final ProductRepository repository;

    @Override
    public void run(String... args) throws Exception {

        //Run only once to populate data to topic
        List<Product> products = repository.findAll();

        for (Product product : products) {
            publisher.publish(product.getProductId(), product);
        }

    }
}