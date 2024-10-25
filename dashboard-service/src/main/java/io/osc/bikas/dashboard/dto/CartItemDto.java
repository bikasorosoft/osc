package io.osc.bikas.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String userId;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("categoryId")
    private String categoryId;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productPrice")
    private Double price;
    @JsonProperty("quantity")
    private Integer quantity;

    public CartItemDto(String userId, Integer quantity, ProductDto productDto) {
        this.userId = userId;
        this.productId = productDto.productId();
        this.categoryId = productDto.categoryId();
        this.productName = productDto.productName();
        this.price = productDto.productPrice();
        this.quantity = quantity;
    }

}
