package io.osc.bikas.user.data.grpc;

import com.google.protobuf.Empty;
import com.osc.bikas.proto.CreateUserRequest;
import com.osc.bikas.proto.UserDataServicesGrpc;
import com.osc.bikas.proto.UserExistsRequest;
import com.osc.bikas.proto.UserExistsResponse;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.user.data.model.User;
import io.osc.bikas.user.data.service.UserService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Date;

@GrpcService
@RequiredArgsConstructor
public class GrpcUserService extends UserDataServicesGrpc.UserDataServicesImplBase {

    private final UserService userService;

    @Override
    public void userExists(UserExistsRequest request, StreamObserver<UserExistsResponse> responseObserver) {
        responseObserver.onNext(UserExistsResponse.newBuilder().setUserExists(userService.userExists(request.getEmail())).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<Empty> responseObserver) {
        var user = request.getUser();
        User user1 = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .contactNumber(user.getContactNumber())
                .dateOfBirth(
                        new Date(user.getDateOfBirth().getSeconds() * 1_000)
                )
                .password(user.getPassword()).build();
        userService.create(user1);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
