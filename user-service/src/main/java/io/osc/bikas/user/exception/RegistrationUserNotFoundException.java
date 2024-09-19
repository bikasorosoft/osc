package io.osc.bikas.user.exception;

public class RegistrationUserNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Registration user %s is not found";

    public RegistrationUserNotFoundException(String msg) {
        super(String.format(ERROR_MESSAGE, msg));
    }
}
