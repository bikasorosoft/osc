package io.osc.bikas.dashboard.dto;

import lombok.Builder;

@Builder
public record DashboardRequestDto(String userId, String sessionId) {
}
