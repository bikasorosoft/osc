package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByCategoryName(String name);
}
