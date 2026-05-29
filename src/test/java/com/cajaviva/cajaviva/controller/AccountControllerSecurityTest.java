package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.security.RestAccessDeniedHandler;
import com.cajaviva.cajaviva.auth.security.RestAuthenticationEntryPoint;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.config.SecurityConfig;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.service.AccountService;
import com.cajaviva.cajaviva.service.LiquidityProjectionService;
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

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
class AccountControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean private AccountService accountService;
    @MockBean private LiquidityProjectionService liquidityProjectionService;
    @MockBean private JwtService jwtService;

    private final UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000002");

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
    @WithAuthenticatedUser
    void getAllAccounts_ReturnsOnlyCurrentUserAccounts() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(currentUserId);
        when(accountService.findByUserId(currentUserId)).thenReturn(List.of(account));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getAccountById_OwnAccount_Returns200() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(currentUserId);
        when(accountService.findById(accountId)).thenReturn(account);

        mockMvc.perform(get("/accounts/" + accountId))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getAccountById_OtherUsersAccount_Returns403() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(otherUserId);
        when(accountService.findById(accountId)).thenReturn(account);

        mockMvc.perform(get("/accounts/" + accountId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void getAccountsByUser_OwnUser_Returns200() throws Exception {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setUserId(currentUserId);
        when(accountService.findByUserId(currentUserId)).thenReturn(List.of(account));

        mockMvc.perform(get("/accounts/user/" + currentUserId))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedUser
    void getAccountsByUser_OtherUser_Returns403() throws Exception {
        mockMvc.perform(get("/accounts/user/" + otherUserId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedUser
    void createAccount_ForcesUserIdToCurrentUser() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(currentUserId);
        when(accountService.create(any(Account.class))).thenAnswer(invocation -> {
            Account arg = invocation.getArgument(0);
            arg.setId(accountId);
            return arg;
        });

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cuenta\",\"userId\":\"" + otherUserId + "\",\"accountType\":1,\"balance\":10}")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    void authenticatedEndpointsAreAccessible() throws Exception {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setUserId(currentUserId);
        when(accountService.findByUserId(currentUserId)).thenReturn(List.of(account));
        when(accountService.findById(id)).thenReturn(account);
        when(accountService.create(any(Account.class))).thenReturn(account);
        when(accountService.update(any(UUID.class), any(Account.class))).thenReturn(account);

        mockMvc.perform(get("/accounts")).andExpect(status().isOk());
        mockMvc.perform(get("/accounts/" + id)).andExpect(status().isOk());
        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Cuenta\",\"userId\":\"" + currentUserId + "\",\"accountType\":1,\"balance\":10}").with(csrf())).andExpect(status().isCreated());
        mockMvc.perform(put("/accounts/" + id).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Cuenta\",\"userId\":\"" + currentUserId + "\",\"accountType\":1,\"balance\":10}").with(csrf())).andExpect(status().isOk());
        mockMvc.perform(delete("/accounts/" + id).with(csrf())).andExpect(status().isNoContent());
    }
}
