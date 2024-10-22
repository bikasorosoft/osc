package io.osc.bikas.kafka.service;

import io.osc.bikas.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class ViewHistoryInteractiveQuery {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    public LinkedList<String> get(String userId) {
        ReadOnlyKeyValueStore<String, LinkedList<String>> store = kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
                KafkaConst.PRODUCT_VIEW_HISTORY,
                QueryableStoreTypes.keyValueStore());
        return store.get(userId);
    }

}
