package io.osc.bikas.dashboard.controller;

import io.osc.bikas.dashboard.dto.*;
import io.osc.bikas.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/user/dashboard")
    public ResponseEntity<ResponseDto> getDashboard(@RequestBody DashboardRequestDto dashboardRequestDto) {
        DataObjectDto dashboardData = dashboardService.getDashboardData(dashboardRequestDto.userId());
        return ResponseEntity.ok(new ResponseDto(200, dashboardData));
    }

    @PostMapping("/product/details")
    public ResponseEntity<ResponseDto> getProductDetailsAndSimilarProducts(@RequestBody ProductDetailsRequestDto productDetailsRequestDto) {
        var data = dashboardService.getProductDetailsAndSimilarProduct(productDetailsRequestDto.productId(), productDetailsRequestDto.categoryId());
        return ResponseEntity.ok(new ResponseDto(200, data));
    }

    @PostMapping("/filter/product")
    public ResponseEntity<ResponseDto> getFilteredProducts(@RequestBody FilterProductRequest filterProductRequest) {
        var responseData = dashboardService.getFilteredProduct(filterProductRequest.categoryId(), filterProductRequest.filter());
        return ResponseEntity.ok(new ResponseDto(200, responseData));
    }
}
