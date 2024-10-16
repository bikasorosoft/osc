package io.osc.bikas.dashboard.controller;

import io.osc.bikas.dashboard.dto.FilterProductRequest;
import io.osc.bikas.dashboard.dto.ProductDetailsRequestDto;
import io.osc.bikas.dashboard.dto.ResponseDto;
import io.osc.bikas.dashboard.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/details")
    public ResponseEntity<ResponseDto> getProductDetailsAndSimilarProducts(@RequestBody ProductDetailsRequestDto productDetailsRequestDto) {
        var data = productService.getProductDetailsAndSimilarProduct(productDetailsRequestDto.productId(), productDetailsRequestDto.categoryId());
        return ResponseEntity.ok(new ResponseDto(200, data));
    }

    @PostMapping("/filter/product")
    public ResponseEntity<ResponseDto> getFilteredProducts(@RequestBody FilterProductRequest filterProductRequest) {
        var responseData = productService.getProductsFilterBy(filterProductRequest.categoryId(), filterProductRequest.filter());
        return ResponseEntity.ok(new ResponseDto(200, responseData));
    }

}
