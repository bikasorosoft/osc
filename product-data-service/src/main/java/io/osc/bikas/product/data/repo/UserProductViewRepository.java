package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.model.UserProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProductViewRepository extends JpaRepository<UserProductView, Integer> {

    Optional<UserProductView> findByUserIdAndProduct(String userId, Product product);

}
