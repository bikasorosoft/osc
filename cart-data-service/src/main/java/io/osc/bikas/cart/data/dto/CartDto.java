package io.osc.bikas.cart.data.dto;

import lombok.Builder;

@Builder
public record CartDto(String userId, String productId, Integer quantity) {
}
