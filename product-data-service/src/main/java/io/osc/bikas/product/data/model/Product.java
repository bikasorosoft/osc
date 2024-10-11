package io.osc.bikas.product.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private Category category;

    private String productName;

    private BigDecimal productPrice;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Column(columnDefinition = "int default 0")
    private Integer viewCount;

    @JsonIgnore
    private String imagePath;

}
