package io.osc.bikas.user.dto;

import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Integer code;
    private Map<String, Object> dataObject;
}
