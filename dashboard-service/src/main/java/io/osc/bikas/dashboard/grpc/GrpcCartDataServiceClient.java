package io.osc.bikas.dashboard.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    public ArrayList<GetCartItemListResponse.CartItem> getCartItemsDetail(String userId) {
        //TODO
        //handle response and implement methods to get product details form product data service and generate cart details response.
        GetCartItemListResponse cartItemList = cartDataServiceBlockingStub.getCartItemList(StringValue.of(userId));
        return new ArrayList<>(cartItemList.getCartItemsList());
    }

}
