package io.osc.bikas.product.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartProduct {//cart relation to product table

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartProductId;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    private Integer quantity;

}
