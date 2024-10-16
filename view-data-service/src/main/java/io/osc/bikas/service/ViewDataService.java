package io.osc.bikas.service;

import io.osc.bikas.kafka.service.KafkaInteractiveQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewDataService {

    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;

    public List<String> getRecentlyViewedProductIdListBy(String userId) {
        var store = kafkaInteractiveQueryService.getRecentlyViewedProductStore();
        return store.get(userId);
    }

}
