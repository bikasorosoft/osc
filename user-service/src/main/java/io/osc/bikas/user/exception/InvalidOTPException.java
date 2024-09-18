package io.osc.bikas.user.exception;

public class InvalidOTPException extends RuntimeException{
    public InvalidOTPException(String userId) {
        super("Invalid otp attempt for user "+userId);
    }
}
