package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.service.AccountService;
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

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class AccountControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean private AccountService accountService;
    @MockBean private LiquidityProjectionService liquidityProjectionService;
    @MockBean private JwtService jwtService;

    @Test
    void unauthenticatedEndpointsReturn401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/accounts")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/accounts/user/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/accounts/" + id)).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/accounts/" + id).contentType(MediaType.APPLICATION_JSON).content("{}").with(csrf())).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/accounts/" + id).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        when(accountService.findAll()).thenReturn(List.of(account));
        when(accountService.findByUserId(id)).thenReturn(List.of(account));
        when(accountService.findById(id)).thenReturn(account);
        when(accountService.create(any(Account.class))).thenReturn(account);
        when(accountService.update(any(UUID.class), any(Account.class))).thenReturn(account);

        mockMvc.perform(get("/accounts")).andExpect(status().isOk());
        mockMvc.perform(get("/accounts/user/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/accounts/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Cuenta\",\"userId\":\"" + id + "\",\"accountType\":1,\"balance\":10}").with(csrf())).andExpect(status().isCreated());
        mockMvc.perform(put("/accounts/" + id).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Cuenta\",\"userId\":\"" + id + "\",\"accountType\":1,\"balance\":10}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/accounts/" + id).with(csrf())).andExpect(status().isNoContent());
    }
}
