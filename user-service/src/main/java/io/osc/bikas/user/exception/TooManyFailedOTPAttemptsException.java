package io.osc.bikas.user.exception;

public class TooManyFailedOTPAttemptsException extends RuntimeException{
    public static final String ERROR_MESSAGE = "user %s too many invalid OTP attempt";
    public TooManyFailedOTPAttemptsException(String userId) {
        super(String.format(ERROR_MESSAGE, userId));
    }
}
