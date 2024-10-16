package io.osc.bikas.kafka.service;

import com.osc.bikas.avro.UserProductViewTopicValue;
import io.osc.bikas.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KafkaInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    private ReadOnlyKeyValueStore<String, LinkedList<String>> recentlyViewedProductStore;

    public ReadOnlyKeyValueStore<String, LinkedList<String>> getRecentlyViewedProductStore() {
        if(Objects.isNull(recentlyViewedProductStore)) {
            recentlyViewedProductStore = kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
                    KafkaConst.PRODUCT_VIEW_HISTORY,
                    QueryableStoreTypes.keyValueStore()
            );
        }
        return recentlyViewedProductStore;
    }

}
