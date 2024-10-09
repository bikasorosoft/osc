package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductDetailsAndSimilarProductListDto (
        @JsonProperty("prodId") String productId,
        @JsonProperty("catId") String categoryId,
        @JsonProperty("prodName") String productName,
        @JsonProperty("prodDesc") String productDescription,
        @JsonProperty("prodPrice") Double productPrice,
        List<ProductDto> similarProducts) {
}
