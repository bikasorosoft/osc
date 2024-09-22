package io.osc.bikas.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOTPRequest {
    private String userId;

    @JsonProperty("OTP")
    private Integer otp;
}
