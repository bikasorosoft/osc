package io.osc.bikas.user.data.grpc.advice;

import io.osc.bikas.user.data.exception.UnknownUserException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcServiceExceptionHandler {

    @GrpcExceptionHandler(UnknownUserException.class)
    public Status handleUnknownEntities(UnknownUserException e){
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

}
