package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.model.CartProduct;
import io.osc.bikas.product.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    Optional<CartProduct> findByProduct(Product product);
}
