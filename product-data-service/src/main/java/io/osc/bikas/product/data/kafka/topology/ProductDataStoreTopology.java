package io.osc.bikas.product.data.kafka.topology;

import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.kafka.KafkaConst;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductDataStoreTopology {

    @Bean
    public KTable<String, ProductDetails> productDataStoreTopology(StreamsBuilder streamsBuilder) {
        KTable<String, ProductDetails> table = streamsBuilder.table(KafkaConst.PRODUCT_DATA_TOPIC,
                Materialized.as(KafkaConst.PRODUCT_DATA_STORE));
        return table;
    }

}
