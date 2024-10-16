package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private String userId;
    private String productId;
    private String categoryId;
    private String productName;
    @JsonProperty("productPrice")
    private Double price;
    @JsonProperty("cartQty")
    private Integer quantity;
}
