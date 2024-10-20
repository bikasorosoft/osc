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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

import static java.time.Duration.ofMinutes;
import static org.apache.kafka.streams.kstream.Suppressed.BufferConfig.unbounded;
import static org.apache.kafka.streams.kstream.Suppressed.untilWindowCloses;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProductClickStreamConfig {

    private final ProductRepository productRepository;

    private static Aggregator<String, Pair, PairList> getPairAggregator() {
        Comparator<Pair> viewCountComparator =
                Comparator
                        .comparingLong(Pair::getViewCount)
                        .reversed()
                        .thenComparing(pair -> pair.getProductId().toString());

        return (String key, Pair pair, PairList aggregate) -> {

            String productId = pair.getProductId().toString();
            TreeSet<Pair> pairTreeSet = new TreeSet<>(viewCountComparator);

            pairTreeSet.addAll(aggregate.getPairList());

            Pair existingPair = pairTreeSet.stream()
                    .filter(item -> item.getProductId().toString().equals(productId))
                    .findFirst().orElse(null);

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
            return PairList.newBuilder().setPairList(pairTreeSet.stream().toList()).build();
        };
    }

    @Bean
    public KTable<Windowed<String>, Long> productClickCountTopology(StreamsBuilder streamsBuilder) {

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConst.SCHEMA_REGISTRY);

        Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        Serde<String> stringValueSerde = new PrimitiveAvroSerde<>();
        stringValueSerde.configure(config, false);

        Serde<Long> longValueSerde = Serdes.Long();
        stringKeySerde.configure(config, false);

        Serde<Windowed<String>> windowedStringSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class);

        Duration WINDOW_DURATION_IN_MIN = ofMinutes(1);

        KStream<String, String> stream =
                streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC, Consumed.with(stringKeySerde, stringValueSerde));

        KTable<Windowed<String>, Long> groupedTable =
                stream.groupBy((userId, productId) -> String.valueOf(productId),
                                Grouped.with(stringKeySerde, stringValueSerde))
                        .windowedBy(TimeWindows.ofSizeWithNoGrace(ofMinutes(1)))
                        .count()
                        .suppress(untilWindowCloses(unbounded()));

        groupedTable.toStream()
                .peek((windowedUserId, viewCountDelta) ->
                        log.info("Processing event for productId: {}, viewCountDelta: {}",
                                windowedUserId.key(), viewCountDelta))
                .foreach((windowedUserId, count) ->
                        updateProductViewCountToDatabase(windowedUserId.key(), count));

        return groupedTable;

    }

    @Transactional
    private void updateProductViewCountToDatabase(String productId, Long viewCountDelta) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cant find product for id: " + productId));
        product.setViewCount(product.getViewCount() + viewCountDelta.intValue());
        productRepository.save(product);
    }

    @Bean
    public KTable<String, PairList> processPopularProductByCategoryTopology(StreamsBuilder streamsBuilder) {

        final Map<String, String> config =
                Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConst.SCHEMA_REGISTRY);

        final Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        final Serde<String> stringValueSerde = new PrimitiveAvroSerde<>();
        stringValueSerde.configure(config, false);

        final SpecificAvroSerde<Pair> pairValueSerde = new SpecificAvroSerde<>();
        pairValueSerde.configure(config, false);

        TreeSetSerde<Pair> treeSetValueSerde = new TreeSetSerde<>(pairValueSerde);
        treeSetValueSerde.configure(config, false);

        final SpecificAvroSerde<PairList> pairListSpecificAvroSerde = new SpecificAvroSerde<>();
        pairListSpecificAvroSerde.configure(config, false);

        Aggregator<String, Pair, PairList> aggregator = getPairAggregator();

        KTable<String, PairList> sortedProductsByCategory =
                streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC,
                                Consumed.with(stringKeySerde, stringValueSerde))
                        .map((userId, productId) ->
                                new KeyValue<>(productId.substring(0, 1), new Pair(productId, 0L)))
                        .groupByKey(Grouped.with(stringKeySerde, pairValueSerde))
                        .aggregate(
                                () -> PairList.newBuilder().setPairList(new ArrayList<>()).build(),
                                aggregator,
                                Materialized.<String, PairList, KeyValueStore<Bytes, byte[]>>as(KafkaConst.POPULAR_PRODUCT_STORE)
                                        .withKeySerde(stringKeySerde)
                                        .withValueSerde(pairListSpecificAvroSerde)
                        );

//        sortedProductsByCategory.toStream();
//                .peek((k, v) -> log.info("categoryId: {}, sortedProductList:{}", k, v));
        return sortedProductsByCategory;
    }
}
