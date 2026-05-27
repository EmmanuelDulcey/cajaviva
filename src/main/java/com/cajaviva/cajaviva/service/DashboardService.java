package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.controller.dto.DashboardSummaryResponse;

import java.util.UUID;

public interface DashboardService {
    DashboardSummaryResponse getSummary(UUID userId, String email);
}
