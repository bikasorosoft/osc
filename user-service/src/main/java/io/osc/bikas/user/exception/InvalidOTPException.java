package io.osc.bikas.user.exception;

public class InvalidOTPException extends RuntimeException{
    public static final String ERROR_MESSAGE = "user %s invalid OTP attempt";
    public InvalidOTPException(String msg) {
        super(String.format(ERROR_MESSAGE, msg));
    }
}
