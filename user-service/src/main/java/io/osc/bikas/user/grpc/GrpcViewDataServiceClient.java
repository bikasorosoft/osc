package io.osc.bikas.user.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.ViewDataServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcViewDataServiceClient {

    @GrpcClient("view-data-service")
    private ViewDataServiceGrpc.ViewDataServiceBlockingStub stub;

    public void saveUserViewHistory(String userId) {
        stub.saveUserViewData(StringValue.of(userId));
    }

}
