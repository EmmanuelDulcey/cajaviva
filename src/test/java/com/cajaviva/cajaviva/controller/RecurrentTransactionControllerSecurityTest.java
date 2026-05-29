package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
import com.cajaviva.cajaviva.service.RecurrentTransactionService;
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

@WebMvcTest(RecurrentTransactionController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class RecurrentTransactionControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean RecurrentTransactionService recurrentTransactionService;
    @MockBean JwtService jwtService;

    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

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
    @WithAuthenticatedUser
    void getAllRecurrentTransactions_ReturnsOnlyCurrentUserTransactions() throws Exception {
        when(recurrentTransactionService.findByUserId(currentUserId)).thenReturn(List.of());
        mockMvc.perform(get("/api/recurrent-transactions")).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getRecurrentTransactionById_OwnTransaction_Returns200() throws Exception {
        UUID txId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(txId);
        tx.setAccount(account);
        when(recurrentTransactionService.findById(txId)).thenReturn(tx);

        mockMvc.perform(get("/api/recurrent-transactions/" + txId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getRecurrentTransactionById_OtherUsersTransaction_Returns403() throws Exception {
        UUID txId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(otherUserId);
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(txId);
        tx.setAccount(account);
        when(recurrentTransactionService.findById(txId)).thenReturn(tx);

        mockMvc.perform(get("/api/recurrent-transactions/" + txId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void getByAccount_OwnAccount_Returns200() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(currentUserId);
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setAccount(account);
        when(recurrentTransactionService.findByAccountId(accountId)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/recurrent-transactions/account/" + accountId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getByAccount_OtherUsersAccount_Returns403() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(otherUserId);
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setAccount(account);
        when(recurrentTransactionService.findByAccountId(accountId)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/recurrent-transactions/account/" + accountId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        RecurrentTransaction tx = new RecurrentTransaction();
        tx.setId(id);
        tx.setAccount(account);
        when(recurrentTransactionService.findByUserId(currentUserId)).thenReturn(List.of(tx));
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
