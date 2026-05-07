package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.service.LiquidityProjectionService;
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

@WebMvcTest(LiquidityProjectionController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class LiquidityProjectionControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean LiquidityProjectionService liquidityProjectionService;
    @MockBean JwtService jwtService;

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/api/liquidity-projections")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/liquidity-projections/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/liquidity-projections/account/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/liquidity-projections").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/liquidity-projections/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/liquidity-projections/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        LiquidityProjection p = new LiquidityProjection();
        p.setId(id);
        when(liquidityProjectionService.findAll()).thenReturn(List.of(p));
        when(liquidityProjectionService.findById(id)).thenReturn(p);
        when(liquidityProjectionService.findByAccountId(id)).thenReturn(List.of(p));
        when(liquidityProjectionService.create(any(LiquidityProjection.class))).thenReturn(p);
        when(liquidityProjectionService.update(any(UUID.class), any(LiquidityProjection.class))).thenReturn(p);

        mockMvc.perform(get("/api/liquidity-projections")).andExpect(status().isOk());
        mockMvc.perform(get("/api/liquidity-projections/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/liquidity-projections/account/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/api/liquidity-projections").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/api/liquidity-projections/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/api/liquidity-projections/" + id).with(csrf())).andExpect(status().isOk());
    }
}
