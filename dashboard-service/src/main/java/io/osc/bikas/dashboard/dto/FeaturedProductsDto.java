package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedProductsDto extends DashboardDto {

    @JsonProperty("TYPE")
    private final String type = "Featured Products";
    @JsonProperty("Featured Products")
    private List<ProductDto> featuredProducts;

}
