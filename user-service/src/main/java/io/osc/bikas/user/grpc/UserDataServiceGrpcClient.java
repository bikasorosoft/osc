package io.osc.bikas.user.grpc;

import com.osc.bikas.proto.UserDataServicesGrpc;
import com.osc.bikas.proto.UserExistsRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceGrpcClient {

    @GrpcClient("user-data-service")
    private UserDataServicesGrpc.UserDataServicesBlockingStub stub;

    public boolean checkEmailExists(String email) {
        var request = UserExistsRequest.newBuilder()
                .setEmail(email)
                .build();
        var response = stub.userExists(request);
        return response.getUserExists();
    }

}
