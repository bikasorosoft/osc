package io.osc.bikas.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("{}", e.getStackTrace());
        ErrorResponse errResponse = new ErrorResponse(500, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(Exception e) {
        log.error("{}", e.getStackTrace());
        ErrorResponse errResponse = new ErrorResponse(30, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserIdNotFoundException(Exception e) {
        log.error("{}", e.getStackTrace());
        ErrorResponse errResponse = new ErrorResponse(1999, e.getMessage());
        return new ResponseEntity<>(errResponse, HttpStatus.NOT_FOUND);
    }

}
