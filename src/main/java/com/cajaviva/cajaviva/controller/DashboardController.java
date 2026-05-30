package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.UserPrincipal;
import com.cajaviva.cajaviva.controller.dto.DashboardSummaryResponse;
import com.cajaviva.cajaviva.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Validated
@Tag(name = "Dashboard", description = "Resumen financiero de la pagina inicial")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener resumen del dashboard",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resumen del dashboard"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    public DashboardSummaryResponse getDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return dashboardService.getSummary(principal.getUserId(), principal.getUsername());
    }
}
