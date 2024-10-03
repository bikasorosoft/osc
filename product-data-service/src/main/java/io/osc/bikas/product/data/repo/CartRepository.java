package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.model.Cart;
import io.osc.bikas.product.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(String userId);
}
