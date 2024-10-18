package io.osc.bikas.product.data.kafka.config;

import com.osc.bikas.avro.Pair;
import com.osc.bikas.avro.PairList;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.kafka.utils.TreeSetSerde;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProductClickStreamConfig {

    private final ProductRepository productRepository;

    @Bean
    public KTable<Windowed<String>, Long> productClickCountTopology(StreamsBuilder streamsBuilder) {

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://192.168.99.223:18081");

        Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        Serde<String> stringValueSerde = new PrimitiveAvroSerde<>();
        stringValueSerde.configure(config, false);

        Duration WINDOW_DURATION_IN_MIN = Duration.ofMinutes(1);

        KStream<String, String> stream = streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC, Consumed.with(stringKeySerde, stringValueSerde));
        var count =  stream
                .groupBy((userId, productId) -> productId)
                .windowedBy(TimeWindows.ofSizeWithNoGrace(WINDOW_DURATION_IN_MIN))
                .count()
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));

        count.toStream()
                .peek((k, v) -> log.info("productId: {}, userId: {}", k, v))
                .foreach(
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

    @Bean
    @Synchronized
    public KTable<String, PairList> processPopularProductByCategoryTopology(StreamsBuilder streamsBuilder) {

        final Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://192.168.99.223:18081");

        final Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        final Serde<String> stringValueSerde = new PrimitiveAvroSerde<>();
        stringValueSerde.configure(config, false);

        final SpecificAvroSerde<Pair> pairValueSerde = new SpecificAvroSerde<>();
        pairValueSerde.configure(config, false);

        final Serde<Long> longValueSerde = new PrimitiveAvroSerde<>();
        longValueSerde.configure(config, false);

        TreeSetSerde<Pair> treeSetValueSerde = new TreeSetSerde<>(pairValueSerde);
        treeSetValueSerde.configure(config, false);

        final SpecificAvroSerde<PairList> pairListSpecificAvroSerde = new SpecificAvroSerde<>();
        pairListSpecificAvroSerde.configure(config, false);

        Aggregator<String, Pair, PairList> aggregator = getPairAggregator();
        KTable<String, PairList> sortedProductsByCategory = streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC,
                        Consumed.with(stringKeySerde, stringValueSerde))
                .map((userId, productId) -> new KeyValue<>(productId.substring(0, 1),
                        Pair.newBuilder().setProductId(productId).setViewCount(0L).build()))
                .groupByKey(Grouped.with(stringKeySerde, pairValueSerde))
                .aggregate(
                        () -> PairList.newBuilder().setPairList(new ArrayList<>()).build(),
                        aggregator,
                        Materialized.<String, PairList, KeyValueStore<Bytes, byte[]>>as(KafkaConst.POPULAR_PRODUCT_STORE)
                                .withKeySerde(stringKeySerde)
                                .withValueSerde(pairListSpecificAvroSerde)
                );
        sortedProductsByCategory.toStream()
                .peek((k, v) -> log.info("categoryId: {}, sortedProductList:{}", k, v));
        return sortedProductsByCategory;
    }

    private static Aggregator<String, Pair, PairList> getPairAggregator() {
        Comparator<Pair> viewCountComparator =
                (Pair pair1,Pair pair2) -> {
                    int viewComparator = Long.compare(pair2.getViewCount(), pair1.getViewCount());
                    int idComparator = pair2.getProductId().toString().compareTo(pair1.getProductId().toString());
                    return viewComparator != 0 ? viewComparator : idComparator;
                };

        Aggregator<String, Pair, PairList> aggregator = (String key, Pair pair, PairList aggregate) -> {

            String productId = pair.getProductId().toString();

            TreeSet<Pair> pairTreeSet = new TreeSet<>(viewCountComparator);

            pairTreeSet.addAll(aggregate.getPairList());

            Pair existingPair = pairTreeSet.stream().filter(item -> item.getProductId().toString().equals(productId))
                    .findFirst()
                    .orElse(null);

            // Remove existing pair if it exists
            if (existingPair != null) {
                pairTreeSet.remove(existingPair);
            }

            // Create a new pair with incremented view count
            Pair updatedPair = Pair.newBuilder()
                    .setProductId(pair.getProductId())
                    .setViewCount(existingPair == null ? 1L : existingPair.getViewCount() + 1L)
                    .build();

            // Add the updated pair back to the TreeSet
            pairTreeSet.add(updatedPair);
            //            log.info("new tree set {}", newSortedList);
            return PairList.newBuilder().setPairList(pairTreeSet.stream().toList()).build();
        };
        return aggregator;
    }
}