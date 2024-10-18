package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c.categoryId FROM Category c")
    List<String> findAllCategoryName();

}
