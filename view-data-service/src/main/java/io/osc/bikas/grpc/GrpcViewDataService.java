package io.osc.bikas.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.osc.bikas.proto.ProductIdList;
import com.osc.bikas.proto.ViewDataServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.osc.bikas.service.ViewDataService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Objects;

@GrpcService
@RequiredArgsConstructor
public class GrpcViewDataService extends ViewDataServiceGrpc.ViewDataServiceImplBase {

    private final ViewDataService viewDataService;

    @Override
    public void getRecentlyViewedProductIdList(StringValue request, StreamObserver<ProductIdList> responseObserver) {

        List<String> userIdList = viewDataService.getRecentlyViewedProductIdListBy(request.getValue());

        ProductIdList response = ProductIdList.newBuilder().build();

        if (!Objects.isNull(userIdList)) {
            List<StringValue> stringValues = userIdList.stream().map(StringValue::of).toList();
            response = ProductIdList.newBuilder().addAllProductId(stringValues).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void saveUserViewData(StringValue request, StreamObserver<Empty> responseObserver) {

        String userId = request.getValue();

        viewDataService.saveUserViewData(userId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }
}
