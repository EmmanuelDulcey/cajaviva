package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.service.LiquidityProjectionService;
import com.cajaviva.cajaviva.support.WithAuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LiquidityProjectionController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class LiquidityProjectionControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean LiquidityProjectionService liquidityProjectionService;
    @MockBean JwtService jwtService;

    private UUID id = UUID.randomUUID();
    private UUID accountId = UUID.randomUUID();
    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private LiquidityProjection createMockProjection() {
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(currentUserId);

        LiquidityProjection p = new LiquidityProjection();
        p.setId(id);
        p.setProjectedBalance(new BigDecimal("5000.00"));
        p.setProjectionDate(LocalDate.of(2026, 6, 15));
        p.setCalculationDate(LocalDateTime.now());
        p.setAccount(account);
        return p;
    }

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        mockMvc.perform(get("/api/liquidity-projections")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/liquidity-projections/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/liquidity-projections/account/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/liquidity-projections").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/liquidity-projections/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/liquidity-projections/" + id).with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/liquidity-projections/calculate").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    void getAllProjections_ReturnsOnlyCurrentUserProjections() throws Exception {
        when(liquidityProjectionService.findByUserId(currentUserId)).thenReturn(List.of());
        mockMvc.perform(get("/api/liquidity-projections")).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getProjectionById_OwnProjection_Returns200() throws Exception {
        UUID projId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setId(projId);
        projection.setAccount(account);
        when(liquidityProjectionService.findById(projId)).thenReturn(projection);

        mockMvc.perform(get("/api/liquidity-projections/" + projId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getProjectionById_OtherUsersProjection_Returns403() throws Exception {
        UUID projId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(otherUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setId(projId);
        projection.setAccount(account);
        when(liquidityProjectionService.findById(projId)).thenReturn(projection);

        mockMvc.perform(get("/api/liquidity-projections/" + projId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void getProjectionsByAccount_OwnAccount_Returns200() throws Exception {
        UUID acctId = UUID.randomUUID();
        Account account = new Account();
        account.setId(acctId);
        account.setUserId(currentUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        when(liquidityProjectionService.findByAccountId(acctId)).thenReturn(List.of(projection));

        mockMvc.perform(get("/api/liquidity-projections/account/" + acctId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getProjectionsByAccount_OtherUsersAccount_Returns403() throws Exception {
        UUID acctId = UUID.randomUUID();
        Account account = new Account();
        account.setId(acctId);
        account.setUserId(otherUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        when(liquidityProjectionService.findByAccountId(acctId)).thenReturn(List.of(projection));

        mockMvc.perform(get("/api/liquidity-projections/account/" + acctId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID pid = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        LiquidityProjection p = new LiquidityProjection();
        p.setId(pid);
        p.setAccount(account);
        when(liquidityProjectionService.findByUserId(currentUserId)).thenReturn(List.of(p));
        when(liquidityProjectionService.findById(pid)).thenReturn(p);
        when(liquidityProjectionService.findByAccountId(accountId)).thenReturn(List.of(p));

        mockMvc.perform(get("/api/liquidity-projections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/liquidity-projections/" + pid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/liquidity-projections/account/" + accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void authenticatedPostWithValidBodyReturnsOk() throws Exception {
        LiquidityProjection p = createMockProjection();
        when(liquidityProjectionService.create(any(LiquidityProjection.class))).thenReturn(p);

        String validJson = """
                {
                    "projectedBalance": 5000.00,
                    "projectionDate": "2026-06-15",
                    "account": {"id": "%s"}
                }
                """.formatted(accountId.toString());

        mockMvc.perform(post("/api/liquidity-projections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.projectedBalance").value(5000.00));
    }

    @Test
    @WithMockUser
    void authenticatedPutWithValidBodyReturnsOk() throws Exception {
        LiquidityProjection p = createMockProjection();
        when(liquidityProjectionService.update(any(UUID.class), any(LiquidityProjection.class))).thenReturn(p);

        String validJson = """
                {
                    "projectedBalance": 7500.00,
                    "projectionDate": "2026-06-20",
                    "account": {"id": "%s"}
                }
                """.formatted(accountId.toString());

        mockMvc.perform(put("/api/liquidity-projections/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @WithMockUser
    void authenticatedDeleteReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/liquidity-projections/" + id).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void calculateEndpointWithValidBodyReturnsOk() throws Exception {
        LiquidityProjection p = createMockProjection();
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        when(liquidityProjectionService.calculateProjection(eq(accountId), eq(startDate), eq(endDate)))
                .thenReturn(List.of(p));

        String calculateJson = """
                {
                    "accountId": "%s",
                    "startDate": "2026-06-01",
                    "endDate": "2026-06-30"
                }
                """.formatted(accountId.toString());

        mockMvc.perform(post("/api/liquidity-projections/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(calculateJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    @WithMockUser
    void getWithNonexistentIdReturnsNotFound() throws Exception {
        when(liquidityProjectionService.findById(id)).thenThrow(new com.cajaviva.cajaviva.exception.ResourceNotFoundException("LiquidityProjection not found"));

        mockMvc.perform(get("/api/liquidity-projections/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    @WithMockUser
    void putWithNonexistentIdReturnsNotFound() throws Exception {
        when(liquidityProjectionService.update(any(UUID.class), any(LiquidityProjection.class)))
                .thenThrow(new com.cajaviva.cajaviva.exception.ResourceNotFoundException("LiquidityProjection not found"));

        String validJson = """
                {
                    "projectedBalance": 5000.00,
                    "projectionDate": "2026-06-15",
                    "account": {"id": "%s"}
                }
                """.formatted(accountId.toString());

        mockMvc.perform(put("/api/liquidity-projections/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }

}
