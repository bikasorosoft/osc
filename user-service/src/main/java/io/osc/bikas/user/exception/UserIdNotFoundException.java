package io.osc.bikas.user.exception;

public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(String id) {
        super("User not found for id "+id);
    }
}
