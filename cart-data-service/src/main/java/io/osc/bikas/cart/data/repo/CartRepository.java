package io.osc.bikas.cart.data.repo;

import io.osc.bikas.cart.data.model.Cart;
import io.osc.bikas.cart.data.model.CartPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, CartPK> {

}
