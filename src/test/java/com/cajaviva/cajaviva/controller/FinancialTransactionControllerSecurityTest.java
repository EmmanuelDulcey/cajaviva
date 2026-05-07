package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.service.FinancialTransactionService;
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

@WebMvcTest(FinancialTransactionController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class FinancialTransactionControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean FinancialTransactionService financialTransactionService;
    @MockBean JwtService jwtService;

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/api/transactions")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/transactions/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/transactions/account/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/transactions/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/transactions/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(id);
        when(financialTransactionService.findAll()).thenReturn(List.of(tx));
        when(financialTransactionService.findById(id)).thenReturn(tx);
        when(financialTransactionService.findByAccountId(id)).thenReturn(List.of(tx));
        when(financialTransactionService.create(any(FinancialTransaction.class))).thenReturn(tx);
        when(financialTransactionService.update(any(UUID.class), any(FinancialTransaction.class))).thenReturn(tx);

        mockMvc.perform(get("/api/transactions")).andExpect(status().isOk());
        mockMvc.perform(get("/api/transactions/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/transactions/account/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content("{\"status\":1}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/api/transactions/" + id).contentType(MediaType.APPLICATION_JSON).content("{\"status\":1}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/api/transactions/" + id).with(csrf())).andExpect(status().isOk());
    }
}
