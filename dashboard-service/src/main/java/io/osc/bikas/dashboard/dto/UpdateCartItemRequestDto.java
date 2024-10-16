package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UpdateCartItemRequestDto(String userId,
                                       @JsonProperty("prodId") String productId,
                                       Integer count) {}
