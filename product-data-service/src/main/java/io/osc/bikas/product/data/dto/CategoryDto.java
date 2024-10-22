package io.osc.bikas.product.data.dto;

import lombok.Builder;

@Builder
public record CategoryDto(String categoryId, String categoryName) {}
