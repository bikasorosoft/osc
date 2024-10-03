package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT c FROM Product p LEFT JOIN p.category c GROUP BY c ORDER BY SUM(p.viewCount) DESC, c.categoryId")
    List<Category> findCategoriesOrderedByProductViewCount();

    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC")
    List<Product> findProductOrderedByProductViewCount();

    List<Product> findByCategoryOrderByProductPriceAsc(Category category);

    List<Product> findByCategoryOrderByProductPriceDesc(Category category);

    List<Product> findByCategoryOrderByViewCountDesc(Category category);

    List<Product> findByCategoryOrderByProductId(Category category);

    List<Product> findByCategoryAndProductIdNot(Category category, String productId);
}