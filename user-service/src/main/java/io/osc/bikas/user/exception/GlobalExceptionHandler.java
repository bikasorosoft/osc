package io.osc.bikas.user.exception;

import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        ErrorResponse errResponse = new ErrorResponse(0, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //this exception we will receive from grpc server
    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleStatusRuntimeException(StatusRuntimeException e){
        log.error("{}", e.getMessage());
        return switch (e.getStatus().getCode()){
            default -> new ResponseEntity<>(new ErrorResponse(0, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUseException(Exception e) {
        log.error("{}", e.getMessage());
        ErrorResponse errResponse = new ErrorResponse(30, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RegistrationUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationUserNotFoundException(Exception e) {
        log.error("{}", e.getStackTrace());
        ErrorResponse errResponse = new ErrorResponse(1999, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooManyFailedOTPAttemptsException.class)
    public ResponseEntity<ErrorResponse> handleTooManyFailedOTPAttemptsException(Exception e) {
        log.error("{}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(301, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidOTPException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOTPException(Exception e) {
        log.error("{}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(502, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

}
