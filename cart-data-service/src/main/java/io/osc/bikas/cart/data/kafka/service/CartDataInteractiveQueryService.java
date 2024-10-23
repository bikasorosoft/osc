package io.osc.bikas.cart.data.kafka.service;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.dto.CartDto;
import io.osc.bikas.cart.data.dto.CartItemDto;
import io.osc.bikas.cart.data.kafka.KafkaConst;
import io.osc.bikas.cart.data.model.Cart;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartDataInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    public List<CartItemDto> get(String userId) {

        ReadOnlyKeyValueStore<String, CartItemList> store =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConst.CART_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
        CartItemList cartItemList = store.get(userId);

        if (cartItemList == null) {
            return new ArrayList<>();
        }

        return generateCartDto(cartItemList.getCartItem());
    }

    private CartItemDto generateCartDto(CartItem cartItem) {
        return CartItemDto.builder()
                .productId(cartItem.getProductId().toString())
                .quantity(cartItem.getCount()).build();
    }

    private List<CartItemDto> generateCartDto(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::generateCartDto)
                .collect(Collectors.toList());
    }

}
