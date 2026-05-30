package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.security.UserPrincipal;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.controller.dto.DashboardSummaryResponse;
import com.cajaviva.cajaviva.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class DashboardControllerSecurityTest {

    @Autowired MockMvc mockMvc;
    @MockBean DashboardService dashboardService;
    @MockBean JwtService jwtService;

    @Test
    void unauthenticatedEndpointReturns401() throws Exception {
        mockMvc.perform(get("/api/dashboard")).andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedEndpointIsAccessible() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "ana@example.com", true);
        DashboardSummaryResponse response = new DashboardSummaryResponse(
                userId,
                "Ana",
                BigDecimal.valueOf(1200),
                "COP",
                BigDecimal.ZERO,
                null,
                List.of(),
                new DashboardSummaryResponse.DashboardLiquidityResponse("Sin datos", List.of()),
                List.of()
        );

        when(dashboardService.getSummary(userId, "ana@example.com")).thenReturn(response);

        mockMvc.perform(get("/api/dashboard").with(authentication(
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greetingName").value("Ana"))
                .andExpect(jsonPath("$.currency").value("COP"));
    }
}
