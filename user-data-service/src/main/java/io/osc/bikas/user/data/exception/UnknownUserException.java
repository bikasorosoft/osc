package io.osc.bikas.user.data.exception;

public class UnknownUserException extends RuntimeException{

    private static final String MESSAGE = "User %s is not found";

    public UnknownUserException(String userId) {
        super(MESSAGE.formatted(userId));
    }
}
