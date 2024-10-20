package io.osc.bikas.product.data.dto;

import lombok.Builder;

@Builder
public record ProductDto(
        String productId,
        String categoryId,
        String productName,
        Double productPrice,
        String productDescription,
        Integer viewCount,
        String imagePath) {
}
