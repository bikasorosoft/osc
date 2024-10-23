package io.osc.bikas.cart.data.service;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.dto.CartItemDto;
import io.osc.bikas.cart.data.kafka.producer.KafkaCartPublisher;
import io.osc.bikas.cart.data.kafka.service.CartDataInteractiveQueryService;
import io.osc.bikas.cart.data.model.Cart;
import io.osc.bikas.cart.data.model.CartPK;
import io.osc.bikas.cart.data.repo.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartDataService {

    private final CartDataInteractiveQueryService cartDataInteractiveQueryService;
    private final KafkaCartPublisher cartPublisher;
    private final CartRepository cartRepository;

    public List<CartItemDto> getCartItemList(String userId) {
        return cartDataInteractiveQueryService.get(userId);
    }

    public void updateCartItem(String userId, String productId, int count) {

        List<CartItemDto> cartItems = cartDataInteractiveQueryService.get(userId);

        CartItemDto cartItem = cartItems.stream()
                .filter(item -> Objects.equals(item.getProductId(), productId))
                .findFirst()
                .orElse(new CartItemDto(productId, 0));

        cartItems.remove(cartItem);

        cartItem.setQuantity(cartItem.getQuantity()+count);

        cartItems.add(cartItem);

        cartPublisher.publish(userId, cartItems);

    }

    public void removeCartItem(String userId, String productId) {

        List<CartItemDto> cartItems = cartDataInteractiveQueryService.get(userId);
        cartItems.removeIf((item) -> Objects.equals(item.getProductId(), productId));

        CartPK cartPK = CartPK.builder().productId(productId).userId(userId).build();
        cartRepository.deleteById(cartPK);

        cartPublisher.publish(userId, cartItems);
    }

    public void updateCartDataToDb(String userId) {

        List<CartItemDto> cartItems = cartDataInteractiveQueryService.get(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            return;
        }

        List<Cart> cartListDto = cartItems.stream()
                .map(cartItem -> new Cart(new CartPK(userId, cartItem.getProductId()), cartItem.getQuantity()))
                .toList();

        cartRepository.saveAll(cartListDto);

    }
}
