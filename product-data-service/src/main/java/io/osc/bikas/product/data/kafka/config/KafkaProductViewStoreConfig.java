package io.osc.bikas.product.data.kafka.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.osc.bikas.product.data.kafka.KafkaConst;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.TimestampedWindowStore;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
public class KafkaProductViewStoreConfig {

    private static Duration windowDuration = Duration.ofMinutes(1);

    @Bean
    public KStream<Windowed<String>, Long> kafkaProductViewStore(StreamsBuilder builder) {

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://192.168.99.223:18081");

        Serde<String> stringKeySerde = Serdes.String();
        stringKeySerde.configure(config, true);

        Serde<String> stringValueSerde = Serdes.String();
        stringKeySerde.configure(config, false);

        Serde<Long> longValueSerde = Serdes.Long();
        longValueSerde.configure(config, false);

        KStream<String, String> stream = builder.stream(KafkaConst.PRODUCT_VIEW_TOPIC, Consumed.with(stringKeySerde,stringValueSerde));

        KTable<Windowed<String>, Long> count = stream
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(windowDuration))
                .count(
                        Materialized.<String, Long,  WindowStore<Bytes, byte[]>>as(KafkaConst.PRODUCT_VIEW_STORE)
                                .withKeySerde(stringKeySerde)
                                .withValueSerde(longValueSerde)
                );

        KStream<Windowed<String>, Long> stream1 = count.toStream();

        stream1.peek(
                (k, v) -> System.out.println(k.key().toString()+","+v)
        );

        stream1.to("test-topic");

        return stream1;

    }

}
