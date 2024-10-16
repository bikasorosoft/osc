package io.osc.bikas.dashboard.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DataObjectDto(List<DashboardDto> data) {}
