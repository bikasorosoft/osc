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
    private List<CartItemDto> cartItemsDtos;
    @JsonProperty("productsCartCount")
    private Integer productsCartCount = 0;
    @JsonProperty("totalPrice")
    private Double totalPrice = 0.0;

    public CartDto(List<CartItemDto> cartItemsDtos) {
        for (CartItemDto cartItemDto: cartItemsDtos) {
            this.productsCartCount += cartItemDto.getQuantity();
            this.totalPrice += cartItemDto.getPrice()*cartItemDto.getQuantity();
        }
        this.cartItemsDtos = cartItemsDtos;
    }

}
