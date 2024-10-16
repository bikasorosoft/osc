package io.osc.bikas.dashboard.grpc;

import com.google.protobuf.BoolValue;
import com.osc.bikas.proto.SessionDataServiceGrpc;
import com.osc.bikas.proto.SessionValidationRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcSessionDataServiceClient {

    @GrpcClient("session-data-service")
    private SessionDataServiceGrpc.SessionDataServiceBlockingStub sessionDataServiceBlockingStub;

    public boolean isSessionValid(String userId, String sessionId) {
        SessionValidationRequest request = SessionValidationRequest.newBuilder().setUserId(userId).setSessionId(sessionId).build();
        BoolValue response = sessionDataServiceBlockingStub.isSessionValid(request);
        return response.getValue();
    }

}
