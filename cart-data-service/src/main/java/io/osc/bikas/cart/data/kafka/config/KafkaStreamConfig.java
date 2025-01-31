package io.osc.bikas.cart.data.kafka.config;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;

import java.util.HashMap;
import java.util.Map;

@EnableKafkaStreams
@Configuration
public class KafkaStreamConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "cart-data-service");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.99.223:19092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put("schema.registry.url", "http://192.168.99.223:18081");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/cart-data");
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0);//1000 ms = 1 sec
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer configurer() {
        return fb -> fb.setStateListener((newState, oldState) -> {
            System.out.println("State transition from " + oldState + " to " + newState);
        });
    }

    @Bean
    public KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService(
            StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService =
                new KafkaStreamsInteractiveQueryService(streamsBuilderFactoryBean);
        return kafkaStreamsInteractiveQueryService;
    }

}
