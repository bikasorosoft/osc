package io.osc.bikas.user.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrpcUserDataServiceClient {

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

    public GetUserPasswordByIdResponse getUserPasswordById(String userId) {

        GetUserPasswordByIdResponse response = stub.getUserPasswordById(StringValue.newBuilder().setValue(userId).build());

        return  response;
    }

}
