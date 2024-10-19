package io.osc.bikas.product.data;

import io.osc.bikas.product.data.kafka.producer.ProductDetailsPublisher;
import io.osc.bikas.product.data.kafka.producer.ProductViewPublisher;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}