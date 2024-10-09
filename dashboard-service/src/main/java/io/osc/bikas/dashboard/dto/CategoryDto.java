package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDto(String categoryId, String categoryName, Integer count) {
}
