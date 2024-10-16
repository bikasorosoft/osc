package io.osc.bikas.dashboard.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import io.osc.bikas.dashboard.dto.CartItemDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrpcCartDataServiceClient {

    @GrpcClient("cart-data-service")
    private CartDataServiceGrpc.CartDataServiceBlockingStub cartDataServiceBlockingStub;

    //update cart product item count
    public void updateCartItem(String userId, String productId, Integer count) {

        UpdateCartItemRequest request =
                UpdateCartItemRequest.newBuilder()
                        .setUserId(userId)
                        .setProductId(productId)
                        .setCount(count)
                        .build();
        cartDataServiceBlockingStub.updateCartItem(request);

    }

    //remove item from cart
    public void removeItemFromCart(String userId, String productId) {
        RemoveCartItemRequest request =
                RemoveCartItemRequest.newBuilder()
                        .setUserId(userId)
                        .setProductId(productId)
                        .build();
        cartDataServiceBlockingStub.removeCartItem(request);
    }

    //get cart products
    public List<CartItemDto> getCartItemsDetail(String userId) {
        GetCartItemListResponse cartItemList = cartDataServiceBlockingStub.getCartItemList(StringValue.of(userId));
        return generateCartITemDto(cartItemList.getCartItemsList());
    }

    private CartItemDto generateCartItemDto(GetCartItemListResponse.CartItem cartItem) {
        return CartItemDto.builder().productId(cartItem.getProductId()).quantity(cartItem.getCount()).build();
    }

    private List<CartItemDto> generateCartITemDto(List<GetCartItemListResponse.CartItem> cartItems) {
        return cartItems.stream()
                .map(this::generateCartItemDto)
                .collect(Collectors.toList());
    }


}
