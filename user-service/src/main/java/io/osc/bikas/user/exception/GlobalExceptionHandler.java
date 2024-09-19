package io.osc.bikas.user.exception;

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

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(Exception e) {
        log.error("{}", e.getMessage());
        ErrorResponse errResponse = new ErrorResponse(30, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(Exception e) {
        log.error("{}", e.getStackTrace());
        ErrorResponse errResponse = new ErrorResponse(199, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxOTPAttemptsExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxOTPAttemptsExceededException(Exception e) {
        log.error("{}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(301, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidOTPException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOTPException(Exception e) {
        log.error("{}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(199, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

}
