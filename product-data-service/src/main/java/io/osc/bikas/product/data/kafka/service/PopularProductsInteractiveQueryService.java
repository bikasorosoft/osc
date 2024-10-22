package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.Pair;
import com.osc.bikas.avro.PairList;
import io.osc.bikas.product.data.dto.PairDto;
import io.osc.bikas.product.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularProductsInteractiveQueryService {

    final static Comparator<Pair> comparator = Comparator.comparingLong(Pair::getViewCount)
            .reversed()
            .thenComparing(item -> item.getProductId().toString());

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    public List<PairDto> get(String categoryId) {
        ReadOnlyKeyValueStore<String, TreeSet<Pair>> store =
                this.kafkaStreamsInteractiveQueryService
                        .retrieveQueryableStore(
                                KafkaConst.POPULAR_PRODUCT_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );
        TreeSet<Pair> pairList = new TreeSet<>(comparator);
        pairList.addAll(store.get(categoryId));
        return generatePairDto(new ArrayList<>(pairList));
    }

    public List<PairDto> getAll() {

        ReadOnlyKeyValueStore<String, TreeSet<Pair>> store =
                this.kafkaStreamsInteractiveQueryService
                        .retrieveQueryableStore(
                                KafkaConst.POPULAR_PRODUCT_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        KeyValueIterator<String, TreeSet<Pair>> pairList = store.all();

        TreeSet<Pair> sortedPair = new TreeSet<>(comparator);
        while (pairList.hasNext()) {
            sortedPair.addAll(pairList.next().value);
        }

        return generatePairDto(sortedPair.stream().toList());

    }

    public List<String> getAllCategoryId() {
        ReadOnlyKeyValueStore<String, TreeSet<Pair>> store =
                this.kafkaStreamsInteractiveQueryService
                        .retrieveQueryableStore(
                                KafkaConst.POPULAR_PRODUCT_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );
        KeyValueIterator<String, TreeSet<Pair>> keyValuePairList = store.all();



        Map<String, Long> categoryCountMap = new HashMap<>();

        while (keyValuePairList.hasNext()) {
            KeyValue<String, TreeSet<Pair>> next = keyValuePairList.next();
            List<Pair> pairList = new ArrayList<>(next.value);
            String categoryId = next.key;

            for (Pair pair : pairList) {
                categoryCountMap.put(categoryId,
                        categoryCountMap.getOrDefault(categoryId, 0L) + pair.getViewCount());
            }
        }
        return categoryCountMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<PairDto> generatePairDto(List<Pair> pairs) {
        return pairs.stream().map(this::generatePairDto)
                .collect(Collectors.toList());
    }

    private PairDto generatePairDto(Pair pair) {
        return PairDto.builder().productId(pair.getProductId().toString()).viewCount(pair.getViewCount()).build();
    }

}
