package io.osc.bikas.user.exception;

public class UserAlreadyExists extends RuntimeException{

    public UserAlreadyExists(String email) {
        super("Email already exists in system: "+email);
    }

}
