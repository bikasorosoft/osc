package io.osc.bikas.user.exception;

public class ForgotPasswordTooManyFailedOTPAttemptsException extends RuntimeException {
    public static final String ERROR_MESSAGE = "forget password user %s too many invalid OTP attempt";
    public ForgotPasswordTooManyFailedOTPAttemptsException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
