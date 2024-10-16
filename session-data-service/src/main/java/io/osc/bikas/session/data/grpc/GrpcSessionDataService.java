package io.osc.bikas.session.data.grpc;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.osc.bikas.proto.*;

import io.grpc.stub.StreamObserver;
import io.osc.bikas.session.data.service.SessionService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GrpcSessionDataService extends SessionDataServiceGrpc.SessionDataServiceImplBase {

    private final SessionService sessionService;

    @Override
    public void sessionExists(SessionExistsRequest request,
                              StreamObserver<BoolValue> responseObserver) {
        String userId = request.getUserId();
        String device = request.getDeviceType();
        boolean response =
                sessionService.sessionExists(userId, device);

        responseObserver.onNext(BoolValue.newBuilder().setValue(response).build());
        responseObserver.onCompleted();

    }

    @Override
    public void createSession(CreateSessionRequest request,
                              StreamObserver<Empty> responseObserver) {
        String sessionId = request.getSessionId();
        String userId = request.getUserId();
        String deviceType = request.getDeviceType();
        sessionService.createSession(sessionId, userId, deviceType);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void logoutSession(LogoutSessionRequest request, StreamObserver<Empty> responseObserver) {
        String sessionId = request.getSessionId();
        String userId = request.getUserId();
        sessionService.logout(sessionId, userId);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void isSessionValid(SessionValidationRequest request, StreamObserver<BoolValue> responseObserver) {

        String userId = request.getUserId();
        String sessionId = request.getSessionId();

        var response = BoolValue.of(sessionService.isSessionValid(userId, sessionId));

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
