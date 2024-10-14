package io.osc.bikas.cart.data.kafka.config;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.kafka.KafkaConst;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaCartStoreConfig {

    @Bean
    public KTable<String, CartItemList> kafkaProductDataStore(StreamsBuilder streamsBuilder) {
        KTable<String, CartItemList> table = streamsBuilder.table(KafkaConst.CART_TOPIC,
                Materialized.as(KafkaConst.CART_STORE));
        return table;
    }

}
