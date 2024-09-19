package io.osc.bikas.user.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    private static final String ERROR_MESSAGE = "email [ %s ] already in use";
    public EmailAlreadyInUseException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
