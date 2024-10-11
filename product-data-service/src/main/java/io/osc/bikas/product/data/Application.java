package io.osc.bikas.product.data;

import io.osc.bikas.product.data.kafka.producer.ProductDetailsPublisher;
import io.osc.bikas.product.data.kafka.producer.ProductViewPublisher;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    ProductDetailsPublisher publisher;
    @Autowired
    ProductRepository repository;

    @Override
    public void run(String... args) throws Exception {

        List<Product> products = repository.findAll();

        for(Product product: products) {
            publisher.publish(product.getProductId(), product);
        }

    }
}