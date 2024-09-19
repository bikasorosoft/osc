package io.osc.bikas.user.grpc;

import com.google.protobuf.Empty;
import com.osc.bikas.proto.CreateUserRequest;
import com.osc.bikas.proto.UpdatePasswordRequest;
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

    public void createUser(CreateUserRequest request) {
        stub.createUser(request);
    }

    public void updatePassword(UpdatePasswordRequest request) {
        stub.updatePassword(request);
    }

}
