package io.osc.bikas.product.data.service;

import com.osc.bikas.avro.ProductAvro;
import io.osc.bikas.product.data.kafka.producer.UserProductViewProducer;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.model.UserProductView;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import io.osc.bikas.product.data.repo.UserProductViewRepository;
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
    private final UserProductViewProducer userProductViewProducer;
    private final UserProductViewRepository userProductViewRepository;

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

    public List<Category> findCategoriesOrderedByProductViewCount() {
        return productRepository.findCategoriesOrderedByProductViewCount();
    }

    public List<Product> findProductOrderedByProductViewCount() {
        return productRepository.findProductOrderedByProductViewCount();
    }

    public List<Product> filterProduct(String categoryId, String filter) {

        Category category = categoryRepository.findById(categoryId).get();

        List<Product> products;

        switch (filter) {
            case "HL":
                products = productRepository.findByCategoryOrderByProductPriceDesc(category);
                break;
            case "LH":
                products = productRepository.findByCategoryOrderByProductPriceAsc(category);
                break;
            case "P":
                products = productRepository.findByCategoryOrderByViewCountDesc(category);
                break;
            case "NF":
                products = productRepository.findByCategoryOrderByProductId(category);
                break;
            default:
                products = productRepository.findAll();
        }
        ;
        return products;
    }

    public Map<String, Object> getProductDetailsWithSimilarAndUpdateViewCount(String userId, String categoryId, String productId) {
        //get product
        Product product = findProductById(productId);
        //update view count
        updateUserProductViewCount(userId, product);
        //get similar products
        List<Product> similarProducts = findSimilarProductByCategory(categoryId, productId);

        Map<String, Object> data = Map.of(
                "prodId", product.getProductId(),
                "catId", product.getCategory().getCategoryId(),
                "prodName", product.getProductName(),
                "prodDesc", product.getProductDescription(),
                "prodPrice", product.getProductPrice(),
                "similarProducts", similarProducts
        );
        return data;
    }

    public Product findProductById(String productId) {
        Product product = productRepository.findById(productId).get();
        return product;
    }

    public void updateUserProductViewCount(String userId, Product product) {

        UserProductView userProductView = userProductViewRepository.findByUserIdAndProduct(userId, product).orElse(
                new UserProductView(null, userId, null, product)
        );

        userProductView.setVisitedAt(LocalDateTime.now());
        userProductViewRepository.save(userProductView);

        //publish product view event
        ProductAvro productAvro = ProductAvro.newBuilder()
                .setProductId(product.getProductId())
                .setCategoryId(product.getCategory().getCategoryId())
                .setProductName(product.getProductName())
                .setProductDescription(product.getProductDescription())
                .setProductPrice(product.getProductPrice().doubleValue())
                .setCount(product.getCount())
                .build();
        userProductViewProducer.sendMessage(userId, productAvro);
    }

    public List<Product> findSimilarProductByCategory(String categoryId, String productId) {
        Category category = categoryRepository.findById(categoryId).get();
        List<Product> productsByCategory = productRepository.findByCategoryAndProductIdNot(category, productId);
        return productsByCategory;
    }
}
