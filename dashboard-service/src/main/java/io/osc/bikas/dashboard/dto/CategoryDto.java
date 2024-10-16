package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDto(String categoryId,
                          @JsonProperty("Category Name") String categoryName,
                          @JsonIgnore
                          Integer count

) {
}
