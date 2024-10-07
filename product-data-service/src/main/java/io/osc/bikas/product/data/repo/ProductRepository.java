package io.osc.bikas.product.data.repo;

import io.osc.bikas.product.data.dto.CategoryDto;
import io.osc.bikas.product.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT new io.osc.bikas.product.data.dto.CategoryDto(c.categoryId, c.categoryName, SUM(p.viewCount)) " +
            "FROM Product p LEFT JOIN p.category c " +
            "GROUP BY c.categoryId, c.categoryName " +
            "ORDER BY SUM(p.viewCount) DESC, c.categoryId")
    List<CategoryDto> findCategoriesOrderedByProductViewCount();

    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC")
    List<Product> findProductOrderedByProductViewCount();

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId ORDER BY p.productPrice ASC")
    List<Product> findByCategoryIdOrderByProductPriceAsc(@Param("categoryId") String categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId ORDER BY p.viewCount ASC")
    List<Product> findByCategoryIdOrderByProductPriceDesc(@Param("categoryId") String categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId ORDER BY p.viewCount DESC")
    List<Product> findByCategoryIdOrderByViewCountDesc(@Param("categoryId") String categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId ORDER BY p.productId DESC")
    List<Product> findByCategoryIdOrderByProductId(@Param("categoryId") String categoryId);

}