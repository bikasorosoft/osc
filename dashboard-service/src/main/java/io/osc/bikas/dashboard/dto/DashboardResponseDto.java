package io.osc.bikas.dashboard.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DashboardResponseDto( List<DashboardDto> data ) {}
