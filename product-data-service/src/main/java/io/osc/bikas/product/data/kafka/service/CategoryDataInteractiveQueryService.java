package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.ProductDetails;
import io.osc.bikas.product.data.dto.ProductDto;
import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryDataInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    public String get(String categoryId) {

        ReadOnlyKeyValueStore<String, String> store = this.kafkaStreamsInteractiveQueryService
                .retrieveQueryableStore(
                        KafkaConst.CATEGORY_DATA_STORE,
                        QueryableStoreTypes.keyValueStore()
                );

        return store.get(categoryId);

    }

}
