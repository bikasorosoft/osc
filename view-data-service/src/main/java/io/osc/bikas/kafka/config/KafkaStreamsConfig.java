package io.osc.bikas.kafka.config;

import com.osc.bikas.avro.UserProductViewTopicValue;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.osc.bikas.kafka.KafkaConst;
import io.osc.bikas.kafka.utils.LinkedListSerde;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;

import java.util.*;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "view-data-service");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.99.223:19092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put("schema.registry.url", "http://192.168.99.223:18081");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/store");
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
    public KTable<String, LinkedList<UserProductViewTopicValue>> kafkaUserProductViewWindowedAggregate(StreamsBuilder builder) {

        final String schemaRegistryUrl = "http://192.168.99.223:18081";

        final Serde<String> stringSerde = Serdes.String();

        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        Serde<String> keySerde = Serdes.String();
        keySerde.configure(serdeConfig, true);

        final Serde<UserProductViewTopicValue> valueSerde = new SpecificAvroSerde();
        valueSerde.configure(serdeConfig, false);

        KStream<String, UserProductViewTopicValue> stream = builder.stream(KafkaConst.USER_PRODUCT_VIEW_TOPIC);
        return stream
                .groupByKey()
                .aggregate(
                        //initializer
                        LinkedList::new,
                        //aggregator
                        (k, v, l) -> {
                            //remove if duplicate present
                            removeDuplicate(v, l);
                            if(l.size()<6) {
                                l.addFirst(v);
                                System.out.println(l.toString());
                                return l;
                            }
                            l.removeLast();
                            l.addFirst(v);
                            System.out.println(l.toString());
                            return l;
                        },
                        Materialized.<String, LinkedList<UserProductViewTopicValue>, KeyValueStore<Bytes, byte[]>>as(KafkaConst.PRODUCT_VIEW_HISTORY)
                                .withKeySerde(keySerde)
                                .withValueSerde(new LinkedListSerde<>(valueSerde))
                );
    }

    private void removeDuplicate(UserProductViewTopicValue v, LinkedList<UserProductViewTopicValue> l) {
        if(l == null || l.isEmpty())    return;

        for(UserProductViewTopicValue i: l) {
            if(Objects.equals(i.getProductId(), v.getProductId())) {
                l.remove(i);
                break;
            }
        }
    }

    @Bean
    public KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService(
            StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService =
                new KafkaStreamsInteractiveQueryService(streamsBuilderFactoryBean);
        return kafkaStreamsInteractiveQueryService;
    }

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(KafkaConst.USER_PRODUCT_VIEW_TOPIC).build();
    }

}
