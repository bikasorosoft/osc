package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record FilterProductRequest(
        String userId,
        @JsonProperty("catId") String categoryId,
        String filter) {}
