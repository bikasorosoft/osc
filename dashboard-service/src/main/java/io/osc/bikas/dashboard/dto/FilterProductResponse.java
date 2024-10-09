package io.osc.bikas.dashboard.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FilterProductResponse(List<ProductDto> products) {
}
