package io.osc.bikas.product.data.kafka.service;

import com.osc.bikas.avro.Pair;
import io.osc.bikas.product.data.dto.PairDto;
import io.osc.bikas.product.data.dto.ProductDto;
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
    private final ProductDetailsInteractiveQuery productDetailsInteractiveQuery;

    public List<ProductDto> getPopularProductsBy(String categoryId) {
        ReadOnlyKeyValueStore<String, TreeSet<Pair>> store =
                this.kafkaStreamsInteractiveQueryService.retrieveQueryableStore(KafkaConst.POPULAR_PRODUCT_STORE, QueryableStoreTypes.keyValueStore());

        TreeSet<Pair> pairs = new TreeSet<>(comparator);

        pairs.addAll(store.get(categoryId));

        return getAllProductsBy(pairs);

    }

    public List<ProductDto> getAllProducts() {

        ReadOnlyKeyValueStore<String, TreeSet<Pair>> store =
                this.kafkaStreamsInteractiveQueryService.retrieveQueryableStore(KafkaConst.POPULAR_PRODUCT_STORE, QueryableStoreTypes.keyValueStore());

        KeyValueIterator<String, TreeSet<Pair>> pairList = store.all();

        TreeSet<Pair> pairs = new TreeSet<>(comparator);
        while (pairList.hasNext()) {
            pairs.addAll(pairList.next().value);
        }

        return getAllProductsBy(pairs);

    }

    public List<String> getAllCategoryId() {
        ReadOnlyKeyValueStore<String, TreeSet<Pair>> store =
                this.kafkaStreamsInteractiveQueryService
                        .retrieveQueryableStore(
                                KafkaConst.POPULAR_PRODUCT_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        KeyValueIterator<String, TreeSet<Pair>> keyValuePairList = store.all();

        return getSortedCategoriesBy(keyValuePairList);
    }

    private List<ProductDto> getAllProductsBy(TreeSet<Pair> pairs) {

        List<ProductDto> products = new ArrayList<>();

        for (Pair pair : pairs) {
            ProductDto product = productDetailsInteractiveQuery.get(pair.getProductId().toString());
            product.setViewCount(pair.getViewCount());
            products.add(product);
        }

        return products;
    }

    private List<String> getSortedCategoriesBy(KeyValueIterator<String, TreeSet<Pair>> keyValuePairList ) {
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

}
