package io.osc.bikas.product.data.dto;

import lombok.Builder;

@Builder
public record PairDto(String productId, Long viewCount) {
}
