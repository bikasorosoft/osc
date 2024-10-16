package io.osc.bikas.dashboard.dto;

import lombok.Builder;

@Builder
public record GetCartRequestDto(String userId) {
}
