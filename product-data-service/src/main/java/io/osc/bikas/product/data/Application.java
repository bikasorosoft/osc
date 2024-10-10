package io.osc.bikas.product.data;

import io.osc.bikas.product.data.kafka.producer.ProductViewPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    ProductViewPublisher publisher;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 1_000; i++) {
            publisher.publish();
            Thread.sleep(1_000);
        }
    }
}