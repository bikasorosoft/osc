package io.osc.bikas.dashboard.grpc;

import com.google.protobuf.StringValue;
import com.osc.bikas.proto.ProductIdList;
import com.osc.bikas.proto.SessionDataServiceGrpc;
import com.osc.bikas.proto.ViewDataServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrpcViewDataServiceClient {

    @GrpcClient("view-data-service")
    private ViewDataServiceGrpc.ViewDataServiceBlockingStub viewDataServiceBlockingStub;

    public List<String> getRecentlyViewedProductIdListBy(String userId) {
        ProductIdList respoinse = viewDataServiceBlockingStub.getRecentlyViewedProductIdList(StringValue.of(userId));

        List<String> productIdList = new ArrayList<>();

        if(respoinse.getProductIdCount() > 0) {
            productIdList = respoinse.getProductIdList().stream().map(StringValue::getValue).toList();
        }
        return productIdList;
    }

}
