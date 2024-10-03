package io.osc.bikas.user.exception;

public class StatusCode {


    public static final Integer INTERNAL_SERVER_ERROR = 0;
    public static final Integer SUCCESS = 200;

    public static final Integer EMAIL_ALREADY_IN_USE = 30;
    public static final Integer EMAIL_NOT_SENT = 220;

    public static final Integer REGISTRATION_USER_NOT_FOUND = 1999;
    public static final Integer INVALID_REGISTRATION_OTP = 502;
    public static final Integer VALID_REGISTRATION_OTP = 500;
    public static final Integer MAX_INVALID_OTP_ATTEMPT_REACHED = 301;

    public static final Integer INVALID_USER_ID = 201;
    public static final Integer INVALID_CREDENTIAL = 202;
    public static final Integer ACTIVE_SESSION_EXISTS_FOR_THIS_DEVICE = 204;

//    public static final Integer MAX_INVALID_PASSWORD_ATTEMPT_REACHED = 205;

    public static final Integer PASSWORD_NOT_SAVED = 199;

}
