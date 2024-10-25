package io.osc.bikas.dashboard.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import io.osc.bikas.dashboard.dto.CartItemQuantityDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

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
    public List<CartItemQuantityDto> getCartItemsDetail(String userId) {
        GetCartItemListResponse cartItemList = cartDataServiceBlockingStub.getCartItemList(StringValue.of(userId));
        return generateCartItemCountDto(cartItemList.getCartItemsList());
    }

    private CartItemQuantityDto generateCartItemCountDto(GetCartItemListResponse.CartItem cartItem) {
        return new CartItemQuantityDto(cartItem.getProductId(), cartItem.getCount());
    }

    private List<CartItemQuantityDto> generateCartItemCountDto(List<GetCartItemListResponse.CartItem> cartItems) {
        return cartItems.stream()
                .map(this::generateCartItemCountDto)
                .collect(Collectors.toList());
    }


}
