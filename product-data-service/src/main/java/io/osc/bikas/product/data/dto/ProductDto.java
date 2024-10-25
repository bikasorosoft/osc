package io.osc.bikas.product.data.dto;

import com.osc.bikas.avro.ProductDetails;
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
    Long viewCount = 0L;

    public ProductDto(String productId, ProductDetails productDetails) {
        this.productId = productId;
        this.categoryId = productId.substring(0, 1);
        this.productName = productDetails.getProductName().toString();
        this.productPrice = productDetails.getProductPrice();
        this.productDescription = productDetails.getProductDescription().toString();
    }

}
