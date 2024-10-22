package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCartDto extends DashboardDto {

    @JsonProperty("TYPE")
    private final String type = "Cart";
    @JsonProperty("Cart")
    private CartDto cartDto;

}
