package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    private ReadOnlyKeyValueStore<String, ProductDetails> productDetailsReadOnlyKeyValueStore;

    public ReadOnlyKeyValueStore<String, ProductDetails> getProductDetailsReadOnlyKeyValueStore() {
        if(productDetailsReadOnlyKeyValueStore == null) {
            this.productDetailsReadOnlyKeyValueStore =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConst.PRODUCT_DATA_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
        }
        return productDetailsReadOnlyKeyValueStore;
    }
}
