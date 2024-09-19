package io.osc.bikas.user.exception;

public class ForgotPasswordInvalidOTPException extends RuntimeException {
    public static final String ERROR_MESSAGE = "forget password user %s invalid OTP attempt";
    public ForgotPasswordInvalidOTPException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
