package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.service.AlertService;
import com.cajaviva.cajaviva.support.WithAuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class AlertControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean AlertService alertService;
    @MockBean JwtService jwtService;

    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/api/alerts")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/alerts/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/alerts/projection/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/alerts").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/alerts/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/alerts/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    void getAllAlerts_ReturnsOnlyCurrentUserAlerts() throws Exception {
        when(alertService.findByUserId(currentUserId)).thenReturn(List.of());
        mockMvc.perform(get("/api/alerts")).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getAlertById_OwnAlert_Returns200() throws Exception {
        UUID alertId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        Alert alert = new Alert();
        alert.setId(alertId);
        alert.setLiquidityProjection(projection);
        when(alertService.findById(alertId)).thenReturn(alert);

        mockMvc.perform(get("/api/alerts/" + alertId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getAlertById_OtherUsersAlert_Returns403() throws Exception {
        UUID alertId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(otherUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        Alert alert = new Alert();
        alert.setId(alertId);
        alert.setLiquidityProjection(projection);
        when(alertService.findById(alertId)).thenReturn(alert);

        mockMvc.perform(get("/api/alerts/" + alertId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void getAlertsByProjection_OwnProjection_Returns200() throws Exception {
        UUID projectionId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        Alert alert = new Alert();
        alert.setLiquidityProjection(projection);
        when(alertService.findByLiquidityProjectionId(projectionId)).thenReturn(List.of(alert));

        mockMvc.perform(get("/api/alerts/projection/" + projectionId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getAlertsByProjection_OtherUsersProjection_Returns403() throws Exception {
        UUID projectionId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(otherUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        Alert alert = new Alert();
        alert.setLiquidityProjection(projection);
        when(alertService.findByLiquidityProjectionId(projectionId)).thenReturn(List.of(alert));

        mockMvc.perform(get("/api/alerts/projection/" + projectionId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        Alert alert = new Alert();
        alert.setId(id);
        alert.setLiquidityProjection(projection);
        when(alertService.findByUserId(currentUserId)).thenReturn(List.of(alert));
        when(alertService.findById(id)).thenReturn(alert);
        when(alertService.findByLiquidityProjectionId(id)).thenReturn(List.of(alert));
        when(alertService.create(any(Alert.class))).thenReturn(alert);
        when(alertService.update(any(UUID.class), any(Alert.class))).thenReturn(alert);

        mockMvc.perform(get("/api/alerts")).andExpect(status().isOk());
        mockMvc.perform(get("/api/alerts/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/alerts/projection/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/api/alerts").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/api/alerts/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/api/alerts/" + id).with(csrf())).andExpect(status().isOk());
    }
}
