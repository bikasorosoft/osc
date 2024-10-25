package io.osc.bikas.kafka.topology;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import io.osc.bikas.kafka.KafkaConst;
import io.osc.bikas.kafka.serdes.LinkedListSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static io.osc.bikas.kafka.KafkaConst.SCHEMA_REGISTRY_URL;

@Configuration
public class ViewHistoryStoreTopology {

    @Bean
    public KTable<String, LinkedList<String>> kafkaUserProductViewWindowedAggregate(StreamsBuilder builder) {

        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);

        final Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(serdeConfig, true);

        final Serde<String> valuePrimitiveAvroSerde = new PrimitiveAvroSerde<>();
        valuePrimitiveAvroSerde.configure(serdeConfig, false);

        final Serde<String> stringValueSerde = Serdes.String();
        stringValueSerde.configure(serdeConfig, false);

        KStream<String, String> stream = builder.stream(KafkaConst.PRODUCT_CLICK_TOPIC,
                Consumed.with(stringKeySerde, valuePrimitiveAvroSerde));
        return stream
                .groupByKey()
                .aggregate(
                        //initializer
                        LinkedList::new,
                        //aggregate
                        (userId, productId, linkedList) -> {
                            linkedList.removeIf((v) -> Objects.equals(v, productId));
                            linkedList.addFirst(productId);
                            while(linkedList.size()>6) {
                                linkedList.removeLast();
                            }
                            System.out.println(linkedList.toString());
                            return linkedList;
                        },
                        //materialize
                        Materialized.<String, LinkedList<String>, KeyValueStore<Bytes, byte[]>>as(KafkaConst.PRODUCT_VIEW_HISTORY)
                                .withKeySerde(stringKeySerde)
                                .withValueSerde(new LinkedListSerde<>(stringValueSerde))
                );
    }
}
