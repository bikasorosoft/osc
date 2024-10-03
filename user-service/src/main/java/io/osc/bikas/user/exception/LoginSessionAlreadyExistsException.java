package io.osc.bikas.user.exception;

public class LoginSessionAlreadyExistsException extends RuntimeException {
    public static final String ERROR_MESSAGE = "Active session already exists for user: ";
    public LoginSessionAlreadyExistsException(String userId) {
        super(String.format(ERROR_MESSAGE, userId));
    }
}
