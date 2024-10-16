package io.osc.bikas.dashboard.controller;

import com.osc.bikas.proto.Cart;
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
        DataObjectDto dashboardData = dashboardService.getDashboardData(dashboardRequestDto.userId(), dashboardRequestDto.sessionId());
        return ResponseEntity.ok(new ResponseDto(200, dashboardData));
    }

}
