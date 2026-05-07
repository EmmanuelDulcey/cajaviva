package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    @WithMockUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        Alert alert = new Alert();
        alert.setId(id);
        when(alertService.findAll()).thenReturn(List.of(alert));
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
