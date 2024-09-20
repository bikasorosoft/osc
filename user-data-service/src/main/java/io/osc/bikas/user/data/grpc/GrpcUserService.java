package io.osc.bikas.user.data.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.proto.*;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.user.data.model.User;
import io.osc.bikas.user.data.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GrpcUserService extends UserDataServicesGrpc.UserDataServicesImplBase {

    private final UserService userService;

    @Override
    public void userExists(UserExistsRequest request, StreamObserver<UserExistsResponse> responseObserver) {
        responseObserver.onNext(UserExistsResponse.newBuilder().setUserExists(userService.userExists(request.getEmail())).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<Empty> responseObserver) {
        User user = User.builder()
                .id(request.getId())
                .name(request.getName())
                .email(request.getEmail())
                .contactNumber(request.getContactNumber())
                .dateOfBirth(
                        LocalDateTime.ofEpochSecond(
                                request.getDateOfBirth().getSeconds(),
                                request.getDateOfBirth().getNanos(),
                                ZoneOffset.UTC
                        ).toLocalDate()
                )
                .password(request.getPassword()).build();
        userService.create(user);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request, StreamObserver<Empty> responseObserver) {
        log.info("update user password");
        userService.updatePassword(request.getEmail(), request.getPassword());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserPasswordById(StringValue request, StreamObserver<GetUserPasswordByIdResponse> responseObserver) {
        String userId = request.getValue();
        userService.findById(userId);
    }
}
