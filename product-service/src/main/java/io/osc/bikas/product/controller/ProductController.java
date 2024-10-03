package io.osc.bikas.product.controller;

import io.osc.bikas.product.model.Category;
import io.osc.bikas.product.model.Product;
import io.osc.bikas.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product> response = productService.findAllProduct();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> findCategoriesOrderedByProductViewCount() {
        List<Category> response = productService.findCategoriesOrderedByProductViewCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Product>> findProductOrderedByProductViewCount() {
        List<Product> response = productService.findProductOrderedByProductViewCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/product")
    public ResponseEntity<Response> filterProduct(@RequestParam(name = "catId") String categoryId,
                                                  @RequestParam(name = "filter") String filter) {
        List<Product> response = productService.filterProduct(categoryId, filter);
        return ResponseEntity.ok(new Response(200, Map.of("products", response)));
    }

    @PostMapping("/user/dashboard")
    public ResponseEntity<Response> getAllProductDetailsForDashboard() {
        Response response = new Response();
        response.setCode(200);
        HashMap<String, Object> dataObject = new HashMap<>();

        List<Map<String, Object>> data = new ArrayList<>();

        //add categories
        List<Category> categories = productService.findCategoriesOrderedByProductViewCount();

        HashMap<String, Object> categoriesData = new HashMap<>();
        categoriesData.put("TYPE", "Categories");
        categoriesData.put("Categories", categories);

        //add featured products
        List<Product> featuredProducts = productService.findProductOrderedByProductViewCount();
        HashMap<String, Object> featuredProductsData = new HashMap<>();

        featuredProductsData.put("TYPE","Featured Products");
        featuredProductsData.put("Featured Products", featuredProducts);

        data.add(categoriesData);
        data.add(featuredProductsData);

        dataObject.put("data", data);

        response.setDataObject(dataObject);

        return ResponseEntity.ok(response);

    }

    //need to refactor to match ui
    @PostMapping("/product/details")
    public ResponseEntity<Response> getProductDetailsWithSimilar(@RequestParam(name = "catId") String categoryId,
                                                       @RequestParam(name = "prodId") String productId,
                                                       @RequestParam(name = "userId") String userId) {

        Map<String, Object> data = productService.getProductDetailsWithSimilarAndUpdateViewCount(userId, categoryId, productId);

        return ResponseEntity.ok(new Response(200, data));

    }

}
