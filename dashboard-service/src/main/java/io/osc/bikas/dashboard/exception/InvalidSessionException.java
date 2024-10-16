package io.osc.bikas.dashboard.exception;

public class InvalidSessionException extends RuntimeException {

    private static final String ERROR_MESSAGE = "invalid session %s for user %s";

    public InvalidSessionException(String userId, String sessionId) {
        super(String.format(ERROR_MESSAGE, sessionId, userId));
    }
}
