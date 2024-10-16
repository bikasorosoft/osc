package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentlyViewedProducts extends DashboardDto {
    @JsonProperty("TYPE")
    private final String type = "Recently Viewed Products";
    List<ProductDto> recentlyViewedProducts;
}
