package io.osc.bikas.product.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    String productId;
    String categoryId;
    String productName;
    Double productPrice;
    String productDescription;
    Long viewCount;
}
