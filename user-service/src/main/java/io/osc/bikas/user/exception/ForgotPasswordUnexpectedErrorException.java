package io.osc.bikas.user.exception;

public class ForgotPasswordUnexpectedErrorException extends RuntimeException {
    public static final String ERROR_MESSAGE = "forgot password user %s password not saved";
    public ForgotPasswordUnexpectedErrorException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
