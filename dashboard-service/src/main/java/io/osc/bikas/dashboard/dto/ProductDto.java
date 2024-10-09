package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record ProductDto(
        String productId,
        String categoryId,
        @JsonProperty("prodName") String productName,
        @JsonProperty("prodMarketPrice") Double productPrice,
        String productDescription,
        @JsonProperty("Counts") Integer viewCount) {
}
