package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProductDetailsRequestDto(
        String userId,
        @JsonProperty("catId") String categoryId,
        @JsonProperty("prodId") String productId) {
}
