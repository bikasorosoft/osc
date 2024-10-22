package io.osc.bikas.user.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.CartDataServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcCartDataServiceClient {

    @GrpcClient("cart-data-service")
    private CartDataServiceGrpc.CartDataServiceBlockingStub stub;

    public void saveUserCart(String userId) {
        stub.saveCart(StringValue.of(userId));
    }

}
