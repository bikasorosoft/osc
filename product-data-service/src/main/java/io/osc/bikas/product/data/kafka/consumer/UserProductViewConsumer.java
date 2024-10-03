package io.osc.bikas.product.data.kafka.consumer;

import com.osc.bikas.avro.ProductAvro;
import io.osc.bikas.product.data.repo.ProductRepository;
import io.osc.bikas.product.data.exception.ProductNotFoundException;
import io.osc.bikas.product.data.kafka.KafkaConstant;
import io.osc.bikas.product.data.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProductViewConsumer {

    private final ProductRepository productRepository;
    private Map<String, Integer> viewCountMap = new ConcurrentHashMap<>();


    @KafkaListener(topics = KafkaConstant.USER_PRODUCT_VIEW_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(List<ConsumerRecord<String, ProductAvro>> list) {
        log.info("start procession ===============================================");
        for (ConsumerRecord<String, ProductAvro> record: list) {
            String key = record.value().getProductId().toString();
            viewCountMap.put(key, viewCountMap.getOrDefault(key, 0)+1);
            log.info("key: {}, value: {}, offset: {}", record.key(), record.value(), record.offset());
        }
        log.info("view count map {}", viewCountMap);
        log.info("procession completed ===========================================");
    }

    @Scheduled(fixedRate = 60000) // Schedule to run every minute
    public void updateProductViewCounts() {
        if (!viewCountMap.isEmpty()) {
            // Batch update the view counts in the database
            for (Map.Entry<String, Integer> click: viewCountMap.entrySet()) {
                String productId = click.getKey();
                Integer count = click.getValue();
                Product product = productRepository.findById(productId)
                        .orElseThrow(()-> new ProductNotFoundException("Product not found for id: " + productId));
                product.setViewCount(product.getViewCount()+count);
                Product save = productRepository.save(product);
            }
            viewCountMap.clear(); // Clear the map after updating
        }
    }
}
