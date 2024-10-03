package io.osc.bikas.product.data;

import com.osc.bikas.avro.Categories;
import com.osc.bikas.avro.CategoryDetails;
import io.osc.bikas.product.data.kafka.producer.CategoriesPublisher;
import io.osc.bikas.product.data.model.Category;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CategoryRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoriesPublisher categoriesPublisher;

    @Override
    public void run(String... args) throws Exception {
        List<Category> categories = repository.findCategoriesOrderedByProductViewCount();
        List<CategoryDetails> categoryDetails = new ArrayList<>();

        for(Category category: categories) {
            categoryDetails.add(toCategoryDetailsFrom(category));
        }

        Categories categoriesAvro = Categories.newBuilder().setCategories(categoryDetails).build();

        categoriesPublisher.publish(categoriesAvro);

    }
    private CategoryDetails toCategoryDetailsFrom(Category category) {
        return CategoryDetails.newBuilder()
                .setCategoryId(category.getCategoryId())
                .setCategoryName(category.getCategoryName())
                .setImagePath(category.getImagePath())
                .build();
    }
}