package io.osc.bikas.user.exception;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class MaxOTPAttemptsExceededException extends RuntimeException{
    public MaxOTPAttemptsExceededException(String userId) {
        super("max otp attempt exceed for user "+userId);
    }
}
