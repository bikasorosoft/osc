package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    @JsonProperty("cartProducts")
    private List<CartItemDto> cartItemsDto;
    @JsonProperty("productsCartCount")
    private Integer productsCartCount;
    @JsonProperty("totalPrice")
    private Double totalPrice;

}
