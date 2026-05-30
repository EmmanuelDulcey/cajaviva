package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.service.FinancialTransactionService;
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

@WebMvcTest(FinancialTransactionController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class FinancialTransactionControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean FinancialTransactionService financialTransactionService;
    @MockBean JwtService jwtService;

    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

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
    @WithAuthenticatedUser
    void getAllTransactions_ReturnsOnlyCurrentUserTransactions() throws Exception {
        when(financialTransactionService.findByUserId(currentUserId)).thenReturn(List.of());
        mockMvc.perform(get("/api/transactions")).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getTransactionById_OwnTransaction_Returns200() throws Exception {
        UUID txId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(txId);
        tx.setAccount(account);
        when(financialTransactionService.findById(txId)).thenReturn(tx);

        mockMvc.perform(get("/api/transactions/" + txId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getTransactionById_OtherUsersTransaction_Returns403() throws Exception {
        UUID txId = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(otherUserId);
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(txId);
        tx.setAccount(account);
        when(financialTransactionService.findById(txId)).thenReturn(tx);

        mockMvc.perform(get("/api/transactions/" + txId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void getTransactionsByAccount_OwnAccount_Returns200() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(currentUserId);
        FinancialTransaction tx = new FinancialTransaction();
        tx.setAccount(account);
        when(financialTransactionService.findByAccountId(accountId)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/transactions/account/" + accountId)).andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getTransactionsByAccount_OtherUsersAccount_Returns403() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(otherUserId);
        FinancialTransaction tx = new FinancialTransaction();
        tx.setAccount(account);
        when(financialTransactionService.findByAccountId(accountId)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/transactions/account/" + accountId)).andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setUserId(currentUserId);
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(id);
        tx.setAccount(account);
        when(financialTransactionService.findByUserId(currentUserId)).thenReturn(List.of(tx));
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
