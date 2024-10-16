package io.osc.bikas.cart.data.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.avro.CartItem;
import com.osc.bikas.avro.CartItemList;
import com.osc.bikas.proto.CartDataServiceGrpc;
import com.osc.bikas.proto.GetCartItemListResponse;
import com.osc.bikas.proto.RemoveCartItemRequest;
import com.osc.bikas.proto.UpdateCartItemRequest;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.cart.data.service.CartDataService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class GrpcCartDataService extends CartDataServiceGrpc.CartDataServiceImplBase {

    private final CartDataService cartDataService;

    @Override
    public void getCartItemList(StringValue request, StreamObserver<GetCartItemListResponse> responseObserver) {

        var cartItems = generateCartItem(cartDataService.getCartItemList(request.getValue()));

        GetCartItemListResponse response = GetCartItemListResponse.newBuilder().addAllCartItems(cartItems).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCartItem(UpdateCartItemRequest request, StreamObserver<Empty> responseObserver) {

        String userId = request.getUserId();
        String productId = request.getProductId();
        int count = request.getCount();

        cartDataService.updateCartItem(userId, productId, count);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }

    @Override
    public void removeCartItem(RemoveCartItemRequest request, StreamObserver<Empty> responseObserver) {

        String userId = request.getUserId();
        String productId = request.getProductId();

        cartDataService.removeCartItem(userId, productId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }

    @Override
    public void saveCart(StringValue request, StreamObserver<Empty> responseObserver) {
        cartDataService.updateCartDataToDb(request.getValue());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }

    List<GetCartItemListResponse.CartItem> generateCartItem(List<CartItem> items) {
        return items.stream()
                .map(this::generateCartItem)
                .collect(Collectors.toList());
    }

    GetCartItemListResponse.CartItem generateCartItem(CartItem item) {
        return GetCartItemListResponse.CartItem.newBuilder()
                .setProductId(item.getProductId().toString())
                .setCount(item.getCount())
                .build();
    }

}
