package io.osc.bikas.cart.data.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.proto.CartDataServiceGrpc;
import com.osc.bikas.proto.GetCartItemListResponse;
import com.osc.bikas.proto.RemoveCartItemRequest;
import com.osc.bikas.proto.UpdateCartItemRequest;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.cart.data.service.CartDataService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GrpcCartDataService extends CartDataServiceGrpc.CartDataServiceImplBase {

    private final CartDataService cartDataService;

    @Override
    public void getCartItemList(StringValue request, StreamObserver<GetCartItemListResponse> responseObserver) {

        cartDataService.getCartItemList(request.getValue());

        GetCartItemListResponse response = GetCartItemListResponse.newBuilder().build();

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
    public void updateCartDataToDb(StringValue request, StreamObserver<Empty> responseObserver) {
        cartDataService.updateCartDataToDb(request.getValue());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }
}
