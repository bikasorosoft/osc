package io.osc.bikas.dashboard.dto;

import lombok.Builder;

@Builder
public record CategoryDto(String categoryId, String categoryName, Integer count) {
}
