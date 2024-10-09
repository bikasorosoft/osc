package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDto(
        String productId,
        String categoryId,
        @JsonProperty("prodName") String productName,
        @JsonProperty("prodMarketPrice") Double productPrice,
        String productDescription,
        @JsonProperty("Counts") Integer viewCount) {
}
