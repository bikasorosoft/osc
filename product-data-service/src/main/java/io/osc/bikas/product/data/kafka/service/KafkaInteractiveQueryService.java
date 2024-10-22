package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.*;
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
    private ReadOnlyKeyValueStore<String, PairList> popularProductReadOnlyKeyValueStore;


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

    public ReadOnlyKeyValueStore<String, PairList> getPopularProductReadOnlyKeyValueStore() {
        if(popularProductReadOnlyKeyValueStore == null) {
            this.popularProductReadOnlyKeyValueStore =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConst.POPULAR_PRODUCT_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
        }
        return popularProductReadOnlyKeyValueStore;
    }



}
