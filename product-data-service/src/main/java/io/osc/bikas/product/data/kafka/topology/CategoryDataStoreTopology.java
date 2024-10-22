package io.osc.bikas.product.data.kafka.topology;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import io.osc.bikas.product.data.kafka.KafkaConst;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CategoryDataStoreTopology {

    @Bean
    public KTable<String, String> processCategoryDataStoreTopology(StreamsBuilder streamsBuilder) {

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConst.SCHEMA_REGISTRY);

        Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config,true);

        Serde<String> stringPrimitiveAvroSerdeValueSerde = new PrimitiveAvroSerde<>();
        stringPrimitiveAvroSerdeValueSerde.configure(config,false);

        Serde<String> stringValueSerde = Serdes.String();
        stringKeySerde.configure(config,false);

        return streamsBuilder.table(KafkaConst.CATEGORY_DATA_TOPIC, Consumed.with(stringKeySerde, stringPrimitiveAvroSerdeValueSerde),
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(KafkaConst.CATEGORY_DATA_STORE)
                        .withKeySerde(stringKeySerde)
                        .withValueSerde(stringValueSerde));
    }

}
