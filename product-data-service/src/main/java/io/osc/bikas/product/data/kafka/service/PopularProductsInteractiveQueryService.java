package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.Pair;
import com.osc.bikas.avro.PairList;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularProductsInteractiveQueryService {

    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;

    public List<String> get(String categoryId) {
        ReadOnlyKeyValueStore<String, PairList> store = kafkaInteractiveQueryService.getPopularProductReadOnlyKeyValueStore();
        PairList pairList = store.get(categoryId);
        return generateProductIdFrom(pairList.getPairList());
    }

    public List<String> getAll() {
        ReadOnlyKeyValueStore<String, PairList> store = kafkaInteractiveQueryService.getPopularProductReadOnlyKeyValueStore();
        KeyValueIterator<String, PairList> pairList = store.all();

        final Comparator<Pair> comparator = Comparator
                .comparingLong(Pair::getViewCount)
                .reversed()
                .thenComparing(item -> item.getProductId().toString());

        TreeSet<Pair> sortedPair = new TreeSet<>(comparator);
        while (pairList.hasNext()) {
            sortedPair.addAll(pairList.next().value.getPairList());
        }

        return generateProductIdFrom(sortedPair.stream().toList());

    }

    private List<String> generateProductIdFrom(List<Pair> pairs) {
        return pairs.stream().map(this::generateProductIdFrom)
                .collect(Collectors.toList());
    }

    private String generateProductIdFrom(Pair pair) {
        return pair.getProductId().toString();
    }

}
