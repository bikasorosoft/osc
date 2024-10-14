package io.osc.bikas.cart.data.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CartPK implements Serializable {

    private String userId;
    private String productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartPK cartPK = (CartPK) o;
        return Objects.equals(userId, cartPK.userId) && Objects.equals(productId, cartPK.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}
