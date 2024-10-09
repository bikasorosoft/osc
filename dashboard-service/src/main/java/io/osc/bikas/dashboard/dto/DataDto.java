package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DataDto (
        @JsonProperty("TYPE") String type,
        List<CategoryDto> categories,
        List<ProductDto> featuredProducts) {
}
