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
        log.error("{}", e);
        ErrorResponse errResponse = new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //this exception we will receive from grpc server
    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleStatusRuntimeException(StatusRuntimeException e){
        log.error("{}", e);
        return switch (e.getStatus().getCode()){
            default -> new ResponseEntity<>(new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUseException(Exception e) {
        log.error("{}", e);
        ErrorResponse errResponse = new ErrorResponse(StatusCode.EMAIL_ALREADY_IN_USE, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RegistrationUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationUserNotFoundException(Exception e) {
        log.error("{}", e);
        ErrorResponse errResponse = new ErrorResponse(StatusCode.REGISTRATION_USER_NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.OK);
    }

    @ExceptionHandler(TooManyFailedOTPAttemptsException.class)
    public ResponseEntity<ErrorResponse> handleTooManyFailedOTPAttemptsException(Exception e) {
        log.error("{}", e);
        ErrorResponse errorResponse = new ErrorResponse(StatusCode.MAX_INVALID_OTP_ATTEMPT_REACHED, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidOTPException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOTPException(Exception e) {
        log.error("{}", e);
        ErrorResponse errorResponse = new ErrorResponse(StatusCode.INVALID_REGISTRATION_OTP, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler({
            ForgotPasswordUserNotFoundException.class,
            ForgotPasswordInvalidOTPException.class,
            ForgotPasswordTooManyFailedOTPAttemptsException.class,
            ForgotPasswordUnexpectedErrorException.class})
    public ResponseEntity<ErrorResponse> handleForgotPasswordException(Exception e) {
        log.error("{}", e);
        ErrorResponse errorResponse = new ErrorResponse(StatusCode.PASSWORD_NOT_SAVED, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(LoginUserIdInvalidException.class)
    public ResponseEntity<ErrorResponse> handleLoginUserIdInvalidException(Exception e) {
        log.error("{}",e);
        ErrorResponse errorResponse = new ErrorResponse(StatusCode.INVALID_USER_ID, e.getMessage());
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(LoginPasswordInvalidException.class)
    public ResponseEntity<ErrorResponse> handleLoginPasswordInvalidException(Exception e) {
        log.error("{}",e);
        ErrorResponse errorResponse = new ErrorResponse(StatusCode.INVALID_CREDENTIAL, e.getMessage());
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(LoginSessionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLoginSessionAlreadyExistsException(LoginSessionAlreadyExistsException e) {
        log.error("{}", e);
        ErrorResponse errorResponse = new ErrorResponse(StatusCode.ACTIVE_SESSION_EXISTS_FOR_THIS_DEVICE, e.getMessage());
        return ResponseEntity.ok(errorResponse);
    }

}
