package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesDto extends DashboardDto {
    @JsonProperty("TYPE")
    private final String type = "Categories";
    @JsonProperty("Categories")
    private List<CategoryDto> categories;
}
