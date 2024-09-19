package io.osc.bikas.user.exception;

public class InvalidOTPException extends RuntimeException{
    public InvalidOTPException(String msg) {
        super("Invalid otp attempt -> "+msg);
    }
}
