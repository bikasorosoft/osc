package io.osc.bikas.cart.data.kafka.schedule;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.dto.CartItemDto;
import io.osc.bikas.cart.data.kafka.service.CartDataInteractiveQueryService;
import io.osc.bikas.cart.data.model.Cart;
import io.osc.bikas.cart.data.model.CartPK;
import io.osc.bikas.cart.data.service.CartDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Component
@RequiredArgsConstructor
@Slf4j
public class CartDataScheduledTask {

    private final CartDataInteractiveQueryService interactiveQueryService;
    private final CartDataService cartDataService;

    @Scheduled(fixedRate = 60_000, initialDelay = 60_000)
    public void saveCartDataToDb() {

        log.info("Starting scheduled task to update cart data to DB.");
        KeyValueIterator<String, CartItemList> keyValueIterator = interactiveQueryService.getAll();

        ArrayList<Cart> cartList = new ArrayList<>();

        while (keyValueIterator.hasNext()) {

            KeyValue<String, CartItemList> keyValue =
                    keyValueIterator.next();

            String userId = keyValue.key;
            CartItemList value = keyValue.value;


            if (value == null || value.getCartItem().isEmpty()) {
                log.warn("No cart items found for userId: {}. Skipping.", userId);
                continue;
            }

            log.info("Processing userId: {}, cartItems count: {}", userId, value.getCartItem().size());

            cartList.addAll(generateCart(userId, generateCartDto(value.getCartItem())));

        }

        if (!cartList.isEmpty()) {
            log.info("Saving {} cart items to DB.", cartList.size());
            cartDataService.saveAll(cartList);
        } else {
            log.info("No cart items to save to DB.");
        }


    }

    private List<Cart> generateCart(String userId, List<CartItemDto> cartItemDtos) {
        log.info("Generating cart entries for userId: {}", userId);
        List<Cart> cartListDto = cartItemDtos.stream()
                .map(cartItem -> new Cart(new CartPK(userId, cartItem.getProductId()), cartItem.getQuantity()))
                .toList();
        return cartListDto;
    }

    private CartItemDto generateCartDto(CartItem cartItem) {
        log.debug("Generating CartItemDto for productId: {}", cartItem.getProductId());
        return CartItemDto.builder()
                .productId(cartItem.getProductId().toString())
                .quantity(cartItem.getCount()).build();
    }

    private List<CartItemDto> generateCartDto(List<CartItem> cartItems) {
        log.debug("Generating list of CartItemDtos for {} items.", cartItems.size());
        return cartItems.stream()
                .map(this::generateCartDto)
                .collect(Collectors.toList());
    }

}
