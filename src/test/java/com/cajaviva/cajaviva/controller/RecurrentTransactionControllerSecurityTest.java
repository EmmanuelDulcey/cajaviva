package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.service.RecurrentTransactionService;
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

@WebMvcTest(RecurrentTransactionController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class RecurrentTransactionControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean RecurrentTransactionService recurrentTransactionService;
    @MockBean JwtService jwtService;

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/api/recurrent-transactions")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/recurrent-transactions/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/recurrent-transactions/account/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/recurrent-transactions").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/recurrent-transactions/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/recurrent-transactions/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(id);
        when(recurrentTransactionService.findAll()).thenReturn(List.of(tx));
        when(recurrentTransactionService.findById(id)).thenReturn(tx);
        when(recurrentTransactionService.findByAccountId(id)).thenReturn(List.of(tx));
        when(recurrentTransactionService.create(any(RecurrentTransaction.class))).thenReturn(tx);
        when(recurrentTransactionService.update(any(UUID.class), any(RecurrentTransaction.class))).thenReturn(tx);

        mockMvc.perform(get("/api/recurrent-transactions")).andExpect(status().isOk());
        mockMvc.perform(get("/api/recurrent-transactions/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/api/recurrent-transactions/account/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/api/recurrent-transactions").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(put("/api/recurrent-transactions/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/api/recurrent-transactions/" + id).with(csrf())).andExpect(status().isOk());
    }
}
