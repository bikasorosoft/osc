package io.osc.bikas.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOTPForForgotPasswordRequest {
    private String email;

    @JsonProperty("OTP")
    private Integer otp;
}
