package io.osc.bikas.product.data.kafka.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Map;

import static org.apache.kafka.streams.kstream.Suppressed.BufferConfig.maxBytes;

//@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaProductViewStoreConfig {

    private static final Duration WINDOW_DURATION_IN_MIN = Duration.ofMinutes(1);
    private static final Suppressed.EagerBufferConfig MAX_BUFFER_SIZE_IN_BYTES =
            maxBytes(1_000_000L);

    private final ProductRepository productRepository;

    @Bean
    public KTable<Windowed<String>, Long> kafkaProductViewStore(StreamsBuilder builder) {

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConst.SCHEMA_REGISTRY);

        Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        GenericAvroSerde stringValueSerde = new GenericAvroSerde();
        stringKeySerde.configure(config, false);

        KStream<String, GenericRecord> stream = builder.stream(KafkaConst.PRODUCT_VIEW_TOPIC, Consumed.with(stringKeySerde, stringValueSerde));
        KTable<Windowed<String>, Long> count = stream
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(WINDOW_DURATION_IN_MIN))
                .count(Materialized.as(KafkaConst.PRODUCT_VIEW_STORE))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));
        count.toStream().foreach(
                (productId, newViewCount) -> updateProductViewCountsDatabase(productId.key(), newViewCount.intValue())
        );

        return count;

    }

    @Transactional
    private void updateProductViewCountsDatabase(String productId, Integer deltaViewCount) {
        log.info("{}, {}", productId, deltaViewCount);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cant find product for id: " + productId));
        log.info("{}", product);
        product.setViewCount(product.getViewCount() + deltaViewCount);
        log.info("{}", product);
        productRepository.save(product);
    }

}
