package io.osc.bikas.cart.data.kafka.service;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.dto.CartDto;
import io.osc.bikas.cart.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartDataInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    public CartItemList get(String userId) {

        ReadOnlyKeyValueStore<String, CartItemList> store =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConst.CART_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
//        return generateCartDto(userId, store.get(userId).getCartItem());4
        return store.get(userId);
    }

    private CartDto generateCartDto(String userId, CartItem cartItem) {
        return CartDto.builder().userId(userId)
                .productId(cartItem.getProductId().toString())
                .quantity(cartItem.getCount()).build();
    }

    private List<CartDto> generateCartDto(String userId, List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> generateCartDto(userId, item))
                .collect(Collectors.toList());
    }

}
