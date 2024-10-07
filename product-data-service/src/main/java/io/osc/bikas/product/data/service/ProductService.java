package io.osc.bikas.product.data.service;

import com.osc.bikas.avro.ProductAvro;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

//    public List<Map<String, Object>> findAllProductDetailsForDashboard(String userId, String sessionId) {
//        //TODO
//
//        //validate session
//
//        //check if user have view history
//
//        //generate response if user dont have view history
//
//        //generate response if user have view history
//    }

    public Product findProductById(String productId) {
        Product product = productRepository.findById(productId).get();
        return product;
    }
}
