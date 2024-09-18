package io.osc.bikas.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOTPRequest {
    private String userId;
    private Integer otp;
}
