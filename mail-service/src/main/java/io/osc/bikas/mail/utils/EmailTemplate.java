package io.osc.bikas.mail.utils;

public class EmailTemplate {

    public static String generateOtpEmail(String userId, Integer otpCode) {
        String greetings = "Hello,\n\n";

        String textBody = String.format(
                "Hello,\n\n" +
                        "Your user ID: %s\n" +
                        "Your OTP code is: %d\n\n" +
                        "Please use this OTP to complete your verification process.\n\n" +
                        "Best regards,\n" +
                        "Orosoft Technologies",
                userId, otpCode
        );
        return textBody;
    }

    public static String generateOtpEmail(Integer otpCode) {
        String greetings = "Hello,\n\n";

        String textBody = String.format(
                "Hello,\n\n" +
                        "Your OTP code is: %d\n\n" +
                        "Please use this OTP to complete your verification process.\n\n" +
                        "Best regards,\n" +
                        "Orosoft Technologies",
                otpCode
        );
        return textBody;
    }
}

