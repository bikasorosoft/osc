package io.osc.bikas.product.data.kafka.topology;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

import static java.time.Duration.ofMinutes;
import static org.apache.kafka.streams.kstream.Suppressed.BufferConfig.unbounded;
import static org.apache.kafka.streams.kstream.Suppressed.untilWindowCloses;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ProductClickTopology {

    private final ProductRepository productRepository;

    @Bean
    public KStream<Windowed<String>, Long> processProductClickCountTopology(StreamsBuilder streamsBuilder) {

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConst.SCHEMA_REGISTRY);

        Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        Serde<String> stringValueSerde = new PrimitiveAvroSerde<>();
        stringValueSerde.configure(config, false);

        Serde<Long> longValueSerde = Serdes.Long();
        stringKeySerde.configure(config, false);

        Serde<Windowed<String>> windowedStringSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class);

        final Duration WINDOW_DURATION_IN_MIN = ofMinutes(1);
        final Duration WINDOW_DURATION_IN_MS = Duration.ofMillis(50_000);

        KStream<String, String> stream =
                streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC, Consumed.with(stringKeySerde, stringValueSerde)
                        .withName("bikas-update-click-count-to-db"));

        KStream<Windowed<String>, Long> stream1 = stream.groupBy((userId, productId) -> String.valueOf(productId),
                        Grouped.with(stringKeySerde, stringValueSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(WINDOW_DURATION_IN_MS))
                .count()
                .suppress(untilWindowCloses(unbounded()))
                .toStream();
                stream1.foreach((windowedProductId, count) ->
                        updateProductViewCountToDatabase(String.valueOf(windowedProductId.key()), count));
        return stream1;

    }

    @Transactional
    private void updateProductViewCountToDatabase(final String productId , final Long viewCountDelta) {
        if (productId == null || viewCountDelta == null) {
            return;
        }
        var optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            log.warn("Product with ID: {} not found. Skipping update.", productId);
            return;
        }
        var product = optionalProduct.get();
        product.setViewCount(product.getViewCount() + viewCountDelta.intValue());
        productRepository.save(product);
    }
}
