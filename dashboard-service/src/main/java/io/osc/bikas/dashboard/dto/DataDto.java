package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record DataDto (
        @JsonProperty("TYPE") String type,
        List<CategoryDto> categories,
        List<ProductDto> featuredProducts) {
}
