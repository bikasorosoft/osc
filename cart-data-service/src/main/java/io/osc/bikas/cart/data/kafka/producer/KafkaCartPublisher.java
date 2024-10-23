package io.osc.bikas.cart.data.kafka.producer;

import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import io.osc.bikas.cart.data.dto.CartItemDto;
import io.osc.bikas.cart.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KafkaCartPublisher {

    private final KafkaTemplate<String, CartItemList> kafkaTemplate;

    public void publish(String userID, List<CartItemDto> cartItemDtos) {

        var key = userID;
        var value = CartItemList.newBuilder().setCartItem(generateCartItem(cartItemDtos)).build();

        kafkaTemplate.send(KafkaConst.CART_TOPIC, key, value);

    }

    private CartItem generateCartItem(CartItemDto cartItemDto) {
        return CartItem.newBuilder().setProductId(cartItemDto.getProductId())
                .setCount(cartItemDto.getQuantity())
                .build();
    }
    private List<CartItem> generateCartItem(List<CartItemDto> cartItemDtos) {
        return cartItemDtos.stream().map(this::generateCartItem)
                .collect(Collectors.toList());
    }

}
