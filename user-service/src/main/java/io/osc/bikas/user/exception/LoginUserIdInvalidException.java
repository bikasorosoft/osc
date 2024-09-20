package io.osc.bikas.user.exception;

public class LoginUserIdInvalidException extends RuntimeException {
    public static final String ERROR_MESSAGE = "login user id %s is invalid";
    public LoginUserIdInvalidException(String userId) {
        super(String.format(ERROR_MESSAGE, userId));
    }
}
