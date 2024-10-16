package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Objects;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDto(
        String productId,
        String categoryId,
        @JsonProperty("prodName") String productName,
        @JsonProperty("prodMarketPrice") Double productPrice,
        @JsonIgnore
        String productDescription,
        @JsonIgnore
        @JsonProperty("Counts") Integer viewCount) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDto that)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}
