package io.osc.bikas.cart.data.service;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.kafka.producer.KafkaCartPublisher;
import io.osc.bikas.cart.data.kafka.service.CartDataInteractiveQueryService;
import io.osc.bikas.cart.data.model.Cart;
import io.osc.bikas.cart.data.model.CartPK;
import io.osc.bikas.cart.data.repo.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartDataService {

    private final CartDataInteractiveQueryService cartDataInteractiveQueryService;
    private final KafkaCartPublisher cartPublisher;
    private final CartRepository cartRepository;

    public List<CartItem> getCartItemList(String userId) {
        return cartDataInteractiveQueryService.get(userId).getCartItem();
    }

    public void updateCartItem(String userId, String productId, int count) {
        var cartItemList = cartDataInteractiveQueryService.get(userId);

        if (cartItemList == null) {
            cartItemList = CartItemList.newBuilder().setCartItem(
                    Arrays.asList(new CartItem(productId, count)
            )).build();
        } else {
            boolean itemExist = false;
            List<CartItem> cartItem = cartItemList.getCartItem();
            for (CartItem item : cartItem) {
                if (Objects.equals(item.getProductId().toString(), productId)) {
                    item.setCount(count + item.getCount());
                    itemExist = true;
                    break;
                }
            }
            if (!itemExist) {
                CartItem newItem = new CartItem(productId, count);
                cartItem.add(newItem);
            }
        }

        cartPublisher.publish(userId, cartItemList);
    }

    public void removeCartItem(String userId, String productId) {

        CartItemList cartItemList = cartDataInteractiveQueryService.get(userId);

        List<CartItem> cartItem = new ArrayList<>(cartItemList.getCartItem());
        cartItem.removeIf((item) -> Objects.equals(item.getProductId().toString(), productId));

        cartItemList.setCartItem(cartItem);
        CartPK cartPK = CartPK.builder().productId(productId).userId(userId).build();
        cartRepository.deleteById(cartPK);

        cartPublisher.publish(userId, cartItemList);
    }

    public void updateCartDataToDb(String userId) {

        CartItemList cartItemList = cartDataInteractiveQueryService.get(userId);

        if (cartItemList == null || cartItemList.getCartItem().isEmpty()) {
            return;
        }

        ArrayList<CartItem> cartItems = new ArrayList<>(cartItemList.getCartItem());

        List<Cart> cartListDto = cartItems.stream()
                .map(cartItem -> new Cart(new CartPK(userId, cartItem.getProductId().toString()), cartItem.getCount()))
                .toList();

        cartRepository.saveAll(cartListDto);

    }
}
