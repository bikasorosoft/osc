package io.osc.bikas.user.data.grpc.advice;

import io.osc.bikas.user.data.exception.UnknownUserException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
@Slf4j
public class GrpcServiceExceptionHandler {

    @GrpcExceptionHandler(UnknownUserException.class)
    public Status handleUnknownEntities(UnknownUserException e){
        log.error("{}", e);
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

}
