package io.osc.bikas.user.exception;

public class LoginPasswordInvalidException extends RuntimeException {
    public static final String ERROR_MESSAGE = "login password is invalid for user %s";
    public LoginPasswordInvalidException(String userId) {
        super(String.format(ERROR_MESSAGE, userId));
    }
}
