package io.osc.bikas.user.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User %s is not found";

    public UserNotFoundException(String msg) {
        super(String.format(ERROR_MESSAGE, msg));
    }
}
