package io.osc.bikas.product.data.kafka.topology;

import com.osc.bikas.avro.Pair;
import com.osc.bikas.avro.PairList;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class PopularProductStoreTopology {
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

        final SpecificAvroSerde<PairList> pairListSpecificAvroSerde = new SpecificAvroSerde<>();
        pairListSpecificAvroSerde.configure(config, false);

        return streamsBuilder.stream(KafkaConst.PRODUCT_CLICK_TOPIC,
                        Consumed.with(stringKeySerde, stringValueSerde)
                                .withName("bikas-popular-product-store-process"))
                .map((userId, productId) ->
                        new KeyValue<>(productId.substring(0, 1), new Pair(productId, 0L)))
                .groupByKey(Grouped.with(stringKeySerde, pairValueSerde))
                .aggregate(
                        () -> PairList.newBuilder().setPairList(new ArrayList<>()).build(),
                        getPairAggregator(),
                        Materialized.<String, PairList, KeyValueStore<Bytes, byte[]>>as(KafkaConst.POPULAR_PRODUCT_STORE)
                                .withKeySerde(stringKeySerde)
                                .withValueSerde(pairListSpecificAvroSerde)
                );
    }

    private static Aggregator<String, Pair, PairList> getPairAggregator() {
        Comparator<Pair> viewCountComparator =
                Comparator.comparingLong(Pair::getViewCount)
                        .reversed()
                        .thenComparing(pair -> pair.getProductId().toString());

        return (String key, Pair pair, PairList aggregate) -> {

            Map<String, Pair> pairMap = aggregate.getPairList().stream()
                    .collect(Collectors.toMap(item -> item.getProductId().toString(), item -> item));

            String productId = pair.getProductId().toString();

            // Increment view count or initialize if not present
            Pair updatedPair = pairMap.getOrDefault(productId,
                    Pair.newBuilder()
                            .setProductId(pair.getProductId())
                            .setViewCount(0L)
                            .build());
            // Update the view count
            updatedPair = Pair.newBuilder(updatedPair)
                    .setViewCount(updatedPair.getViewCount() + 1)
                    .build();

            // Put the updated pair back into the map
            pairMap.put(productId, updatedPair);

            // Sort the pairs by view count and product ID after updates
            List<Pair> sortedPairs = pairMap.values().stream()
                    .sorted(viewCountComparator)
                    .collect(Collectors.toList());

            return PairList.newBuilder().setPairList(sortedPairs).build();
        };
    }


    /*
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

     */

}
