package io.osc.bikas.product.data.kafka.topology;

import com.osc.bikas.avro.Pair;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.osc.bikas.product.data.kafka.KafkaConst;
import io.osc.bikas.product.data.kafka.serdes.TreeSetSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class PopularProductStoreTopology {

    private static Aggregator<String, String, TreeSet<Pair>> getPairAggregator() {
        return (String categoryId, String productId, TreeSet<Pair> aggregate) -> {
            Pair existingPair = aggregate.stream()
                    .filter(item -> item.getProductId().toString().equals(productId))
                    .findFirst()
                    .orElse(Pair.newBuilder().setProductId(productId).setViewCount(0).build());
            aggregate.remove(existingPair);
            existingPair.setViewCount(existingPair.getViewCount() + 1L);
            aggregate.add(existingPair);
            return aggregate;
        };
    }

    @Bean
    public KTable<String, TreeSet<Pair>> processPopularProductByCategoryTopology(StreamsBuilder streamsBuilder) {

        final Map<String, String> config =
                Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConst.SCHEMA_REGISTRY);

        final Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        final Serde<String> stringValueSerde = new PrimitiveAvroSerde<>();
        stringValueSerde.configure(config, false);

        final Serde<Pair> pairValueSerde = new SpecificAvroSerde<>();
        pairValueSerde.configure(config, false);

        Comparator<Pair> comparator = Comparator.comparingLong(Pair::getViewCount)
                .reversed()
                .thenComparing(item -> item.getProductId().toString());

        final Serde<TreeSet<Pair>> treeSetValueSerde = new TreeSetSerde<>(pairValueSerde, comparator);
        treeSetValueSerde.configure(config, false);

        KTable<String, TreeSet<Pair>> aggregate = streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC,
                        Consumed.with(stringKeySerde, stringValueSerde)
                                .withName("bikas-popular-product-store-process"))
                .selectKey((k, v) -> v.substring(0, 1))
                .groupByKey(Grouped.with(stringKeySerde, stringValueSerde))
                .aggregate(
                        () -> new TreeSet<>(comparator),
                        getPairAggregator(),
                        Materialized.<String, TreeSet<Pair>, KeyValueStore<Bytes, byte[]>>as(KafkaConst.POPULAR_PRODUCT_STORE)
                                .withKeySerde(stringKeySerde)
                                .withValueSerde(treeSetValueSerde)
                );

        aggregate.toStream()
                .peek((k, v) -> log.info("key: {}, value: {}", k, v.toString()));

        return aggregate;
    }

}
