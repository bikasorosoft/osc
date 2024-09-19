package io.osc.bikas.user.exception;

public class ForgotPasswordUserNotFoundException extends RuntimeException {
    public static final String ERROR_MESSAGE = "forget password user %s not found in system";
    public ForgotPasswordUserNotFoundException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
