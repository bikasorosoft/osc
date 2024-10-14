package io.osc.bikas.dashboard.dto;

import lombok.Builder;

@Builder
public record UpdateCartItemRequestDto(String userId, String productId, Integer count) {}
