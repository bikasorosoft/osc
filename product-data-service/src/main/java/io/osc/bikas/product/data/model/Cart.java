package io.osc.bikas.product.data.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {//user relation to cart

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartId;
    private String userId;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "cartId", referencedColumnName = "cartId")
    private Set<CartProduct> cartProducts;

}
