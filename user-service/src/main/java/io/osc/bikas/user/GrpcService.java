package io.osc.bikas.user;

import com.osc.bikas.proto.UserDataServicesGrpc;
import com.osc.bikas.proto.UserExistsRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcService {

    @GrpcClient("hello")
    private UserDataServicesGrpc.UserDataServicesBlockingStub stub;

    public void someMethod() {
        stub.userExists(
                UserExistsRequest.newBuilder().setEmail("abc@gmail.com").build()
        );
    }

}
