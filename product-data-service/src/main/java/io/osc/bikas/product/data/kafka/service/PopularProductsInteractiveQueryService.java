package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.Pair;
import com.osc.bikas.avro.PairList;
import io.osc.bikas.product.data.dto.PairDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularProductsInteractiveQueryService {

    private final KafkaInteractiveQueryService kafkaInteractiveQueryService;

    public List<PairDto> get(String categoryId) {
        ReadOnlyKeyValueStore<String, PairList> store = kafkaInteractiveQueryService.getPopularProductReadOnlyKeyValueStore();
        PairList pairList = store.get(categoryId);
        return generatePairDto(pairList.getPairList());
    }

    public List<PairDto> getAll() {
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

        return generatePairDto(sortedPair.stream().toList());

    }

    public List<String> getAllCategoryId() {
        ReadOnlyKeyValueStore<String, PairList> store = kafkaInteractiveQueryService.getPopularProductReadOnlyKeyValueStore();
        KeyValueIterator<String, PairList> keyValuePairList = store.all();

        Map<String, Long> categoryCountMap = new HashMap<>();

        while (keyValuePairList.hasNext()) {
            KeyValue<String, PairList> next = keyValuePairList.next();
            List<Pair> pairList = next.value.getPairList();
            String categoryId = next.key;

            for (Pair pair: pairList) {
                categoryCountMap.put(categoryId,
                        categoryCountMap.getOrDefault(categoryId, 0L)+ pair.getViewCount());
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
