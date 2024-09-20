package io.osc.bikas.user.grpc;

import com.google.protobuf.BoolValue;
import com.osc.bikas.proto.CreateSessionRequest;
import com.osc.bikas.proto.SessionDataServiceGrpc;
import com.osc.bikas.proto.SessionExistsRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcSessionDataServiceClient {

    @GrpcClient("session-data-service")
    private SessionDataServiceGrpc.SessionDataServiceBlockingStub stub;


    public Boolean sessionExists(String userId, String deviceType) {
        SessionExistsRequest request = SessionExistsRequest.newBuilder().setUserId(userId).setDeviceType(deviceType).build();
        Boolean response = stub.sessionExists(request).getValue();
        return response;
    }

    public void createSession(String sessionId, String userId, String deviceType) {
        CreateSessionRequest createSessionRequest = CreateSessionRequest.newBuilder()
                .setSessionId(sessionId)
                .setUserId(userId)
                .setDeviceType(deviceType)
                .build();
        stub.createSession(createSessionRequest);
    }
}
